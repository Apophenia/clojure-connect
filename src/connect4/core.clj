(ns connect4.core
  (:gen-class))

(def string-map
  {1 "X", -1 "O", 0 "_" :left-pipe "|" :right-pipe "|"})

(def num-rows 6)

(def num-columns 7)

(def initial-grid (vec (repeat num-rows (vec (repeat num-columns 0)))))

(defn get-cell-value
  "Returns the value of a given cell"
  [current-grid [x y]]
  (nth (nth current-grid y) x))

(defn get-column [current-grid x]
  (vec (map #(nth % x) current-grid)))

(defn get-run-vals [run grid]
  "Returns the values of a run as a vector => [1 1 1 1]"
  (for [coords run]
    (get-cell-value grid coords)))

(defn up-diag [[x y]] [(inc x)(inc y)])
(defn down-diag [[x y]] [(inc x)(dec y)])
(defn horizontal [[x y]] [(inc x) y])
(defn vertical [[x y]] [x (inc y)])

(defn run-from [x y f]
  "(run-from 0 0 up-diag) -> ([0 0] [1 1] [2 2] [3 3])"
  (take 4 (iterate f [x y])))

(defn generate-runs [f x-range y-range]
  "Generates a list of all possible up-diagonals"
  (for [x x-range
        y y-range]
    (run-from x y f)))

(def all-runs
  (concat (generate-runs up-diag (range 4) (range 3))
          (generate-runs down-diag (range 4) (range 3 6))
          (generate-runs horizontal (range 4) (range 6))
          (generate-runs vertical (range 7)(range 3))))

(defn win-check [current-grid]
  (let [all-run-vals (for [run all-runs]
                       (get-run-vals run current-grid))
        wins (filter (fn [[a b c d]]
                       (and (not (zero? a))
                            (= a b c d)))
                     all-run-vals)]
    (if (not (empty? wins)) (first (first wins))
      nil)))

(defn draw-cells
  "Draws (via printing) a row of cells"
  [row i]
  (if (not= i num-columns)
    (do (print (string-map :left-pipe) (string-map (row i)))
        (recur row (inc i)))
    (print (string-map :right-pipe))))

(defn draw-row
  "Draws (via printing) a horizontal row by calling draw-cells the appropriate number of times"
  [current-grid n]
  (if (<= 0 n)
    (do
      (draw-cells (current-grid n) 0)
      (println)
      (recur current-grid (dec n)))))

(defn draw-grid
  "Draws (via printing) a game grid"
  [current-grid]
  (do (draw-row current-grid (dec num-rows))
      (println "  0  1  2  3  4  5  6")))

(defn set-cell-value
  "Given coordinates and a player value, sets the value of a cell and returns an updated board"
  [current-grid [x y] value]
  (assoc-in current-grid [y x] value))

(defn drop-piece
"Given column number x and a player value, drops a piece into the
lowest available spot in a column and returns the new board"
  [current-grid x value]
       (loop [y 0]
        (cond (>= y num-rows) (throw (Exception. (str "Passed " y " to function")))
              (zero? (get-cell-value current-grid [x y])) (set-cell-value current-grid [x y] value)
              :else (recur (inc y)))))

(defn column-open?
  "Returns true if a column contains a playable space"
  [current-grid x]
  (zero? (get-cell-value current-grid [x (dec num-rows)])))

(defn tie? [current-grid]
(not-any? zero? (current-grid (dec num-rows))))

(defn open-columns [current-grid]
  (filter
   (partial column-open? current-grid)
   (range num-columns)))

(defn analyze-run [run-values v]
  (condp = run-values
    [v v v 0] 5
    [0 v v v] 5
    [v v 0 v] 5
    [v 0 v v] 5
    [v v 0 0] 2
    [v 0 0 v] 2
    [0 0 v v] 2
    [0 v v 0] 3
    0))

(defn analyze-board [current-grid value]
 (let [results (for [run all-runs]
                (analyze-run (get-run-vals run current-grid) value))]
   (reduce + results)))
;; TODO: optimally, pass in just the player values rather than the players themselves.
;; Minimax doesn't need to continually access the values of the records.
(defn minimax*
 ([current-grid current-player other-player]
     (minimax* current-grid current-player other-player 5))
 ([current-grid current-player other-player curr-depth]
     (let [win (win-check current-grid)]
       (cond  (= win (:value current-player)) [nil 1]
              (= win (:value other-player)) [nil -1]
              (tie? current-grid) [nil 0]
              :else
              (if (zero? curr-depth) [nil (analyze-board current-grid (:value current-player))]
                  (let [possible-moves (for [col (open-columns current-grid)
                                             :let [[_col score] (minimax* (drop-piece current-grid col (:value current-player))
                                                                          other-player
                                                                          current-player
                                                                          (dec curr-depth))]]
                                         [col (- score)])]
                    (apply max-key second possible-moves)))))))

(defn minimax [& args]
  (first (apply minimax* args)))

(defn game-over? [current-grid]
  (win-check current-grid))

(defprotocol Player
  "This defines a way to make a move in the game."
  (make-move [this other-player current-grid]))

(defrecord HumanPlayer [player-name value]
  Player
  (make-move [this other-player current-grid]
    (do (println "Make a move please, person.")
    (loop [column-selection (read-string (read-line))]
      (if (and (integer? column-selection) (not (neg? column-selection)) (< column-selection 7))
        (if (column-open? current-grid column-selection) column-selection
            (do (println "Column is full. Select another column.")
                (recur (read-string (read-line)))))
        (do (println "Invalid column choice. Input 0-6.")
            (recur (read-string (read-line)))))))))

(defrecord ComputerPlayer [value]
  Player
  (make-move [this other-player current-grid]
    (minimax current-grid this other-player)))

(defn game-loop [current-grid current-player other-player]
  "Defines the main game automation"
  (if (or (win-check current-grid) (tie? current-grid))
    (do (println "The game is over.")
        (draw-grid current-grid))
    (do (draw-grid current-grid)
        (let [next-grid (drop-piece current-grid (make-move current-player other-player current-grid) (:value current-player))]
          (recur next-grid other-player current-player)))))

(def joe (ComputerPlayer. -1))
(def denis (HumanPlayer. "Denis" 1))

(defn -main [& args]
(game-loop initial-grid joe denis))
