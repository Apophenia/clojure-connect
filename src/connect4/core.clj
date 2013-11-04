(ns connect4.core
  (:gen-class))

(def string-map
 {1 "X", -1 "O", 0 "_" :left-pipe "|" :right-pipe "|"})

(def num-rows 6)

(def num-columns 7)

(def initial-grid (vec (repeat num-rows (vec (repeat num-columns 0)))))

; TEST GRIDS
(def test-bottom-row-o-win (apply conj [[-1 -1 -1 -1 0 0 0]] 
                                       (vec (repeat (dec num-rows) (vec (repeat num-columns 0))))))
(def test-bottom-row-x-win (apply conj [[1 1 1 1 0 0 0]] 
                                       (vec (repeat (dec num-rows) (vec (repeat num-columns 0))))))
(def test-second-row-o-win (apply conj [[-1 1 -1 1 -1 1 -1][1 1 1 -1 -1 -1 -1]] 
                                       (vec (repeat (dec (dec num-rows)) (vec (repeat num-columns 0))))))
(def test-second-row-x-win (apply conj [[1 -1 1 -1 1 -1 -1][-1 -1 -1 1 1 1 1]] 
                                       (vec (repeat (dec (dec num-rows)) (vec (repeat num-columns 0))))))
(def test-third-row-x-win (apply conj  [[1 1 -1 1 1 -1 1][-1 1 1 -1 1 1 -1][1 1 1 1 0 -1 -1 -1]]
                                       (vec (repeat (- num-rows 3)(vec (repeat num-columns 0))))))
(def test-fourth-row-o-win (apply conj [[1 1 -1 1 1 -1 1][-1 1 1 -1 1 1 -1][1 -1 -1 1 -1 -1 1][1 1 1 -1 -1 -1 -1]]
                                       (vec (repeat (- num-rows 4)(vec (repeat num-columns 0))))))
(def test-first-column-o-win (apply conj (repeat 4 [-1 0 0 0 0 0 0]) (repeat 2 [0 0 0 0 0 0 0])))

(defn get-cell-value
"Returns the value of a given cell" 
  [current-grid [x y]]
  ((current-grid y) x))

(defn get-column [current-grid x]
"Returns the specified column in vector form"
  (vec (map #(% x) current-grid)))

(defn win-check [current-grid])
; (cond (every? zero? (current-grid 3)) )

; (def row [0 1 0 0 0 0 0])
; (every? zero? row)

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
  (draw-row current-grid (dec num-rows)))

(defn set-cell-value
"Given coordinates and a player value, sets the value of a cell and returns an updated board"
  [current-grid [x y] value]
   (assoc-in current-grid [y x] value))

(defn drop-piece
"Given column number x and a player value, drops a piece into the lowest available spot in a column and returns the new board"
  [current-grid x value]
  (loop [y 0]
    (cond (>= y num-rows) (throw (Exception. (str "Passed " y " to function")))
          (zero? (get-cell-value current-grid [x y])) (set-cell-value current-grid [x y] value)
          :else (recur (inc y)))))

(defn column-open?
"Returns true if a column contains a playable space"
  [current-grid x]
  (zero? (get-cell-value current-grid [x 0])))

(defn game-over? [current-grid]
  true)

(defprotocol Player
  "This defines a way to make a move in the game."
  (make-move [player-type current-grid]))

(defrecord HumanPlayer [player-name value]
  Player
  (make-move [player-type current-grid] 0))

(defrecord ComputerPlayer [value]
  Player
  (make-move [player-type current-grid] 0))

(defn abs [x]
  (if (neg? x)
      (* -1 x)
      x))

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

(defn get-run-values [run board]



(defn vertical-win [current-grid [x y] value]
  (cond (and (>= y 3) 
        (= (get-cell-value current-grid [x (- y 3)]) value) 
        (= (get-cell-value current-grid [x (- y 2)]) value) 
        (= (get-cell-value current-grid [x (- y 1)]) value)) true
        :else false))

(defn game-loop [current-grid current-player other-player]
"Defines the main game automation" 
 (let [next-grid (drop-piece current-grid (make-move current-player current-grid) (:value current-player))]
   (draw-grid next-grid)
   ;(if (game-over? @grid) 
   ;    (println "The game is over")
    (recur next-grid other-player current-player)))

(defn -main [& args]
;;  (drop-piece @grid 0 -1)
;;  (set-cell-value grid [0 0] -1)
;; (draw-grid test-bottom-row-o-win)
;; (assert (horizontal-win test-bottom-row-o-win [1 0] -1))
;; (draw-grid test-bottom-row-o-win)
;; (assert (horizontal-win test-bottom-row-o-win [0 0] -1))
;; (draw-grid test-bottom-row-x-win)
;; (assert (horizontal-win test-bottom-row-o-win [1 0] -1))
;; (draw-grid test-bottom-row-x-win)
;; (assert (horizontal-win test-bottom-row-x-win [0 0] 1))
;; (draw-grid test-second-row-x-win)
;; (assert (horizontal-win test-second-row-x-win [6 1] 1))
;; (draw-grid test-second-row-o-win)
;; (assert (horizontal-win test-second-row-o-win [5 1] -1))
;; (draw-grid test-third-row-x-win)
;; (assert (horizontal-win test-third-row-x-win [3 2] 1))
;; (draw-grid test-fourth-row-o-win)
;; (assert (horizontal-win test-fourth-row-o-win [4 3] -1))
;; (draw-grid test-first-column-o-win))
;; (assert (vertical-win test-first-column-o-win [0, 0] -1))

)
