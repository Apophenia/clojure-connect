(ns connect4.core
  (:gen-class))

(def string-map
 {1 "X", -1 "O", 0 "_" :left-pipe "|" :right-pipe "|"})

(def num-rows 6)

(def num-columns 7)

(def grid (atom (vec (repeat num-rows (vec (repeat num-columns 0))))))

(defn get-cell-value 
  [current-grid [x y]]
  ((current-grid y) x))

(defn draw-cells 
  [row i]
  (if (not= i num-columns)
  (do (print (string-map :left-pipe) (string-map (row i)))
      (recur row (inc i)))
  (print (string-map :right-pipe)))) 

(defn draw-row
  [current-grid n]
 (if (not= n num-rows) 
  (do (draw-cells (current-grid n) 0)
      (println)
      (recur current-grid (inc n)))))

(defn draw-grid
  [current-grid]
  (draw-row current-grid 0))

(defn set-cell-value
  [current-grid [x y] value]
   (swap! current-grid assoc-in [x y] value))

(defn drop-piece 
  [current-grid x value]
  (loop [y num-rows]
    (cond ((= -1 y) (throw (Exception. "Passed a non-playable column to drop-piece."))
          (zero? (get-cell-value current-grid [x y])) (set-cell-value current-grid [x y] value)
          :else (recur (dec y)))))

(defn column-open?
  [current-grid x]
  (zero? (get-cell-value @current-grid [x 0])))

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

(defn game-loop [player-one player-two]
  (loop [current-player player-one
         other-player player-two]
    (drop-piece @grid (make-move current-player @grid) (:value current-player))
    (if (game-over? @grid) 
      (println "The game is over")
      (recur other-player current-player))))

(defn -main [& args]
  (let [human (HumanPlayer. "Lyndsey" 1)
        computer (ComputerPlayer. -1)]
    (game-loop human computer)))

  (comment (draw-grid @grid)
  (println (get-cell-value @grid [2 2]))
  (set-cell-value grid [2 2] 1)
  (println (get-cell-value @grid [2 2]))
  (draw-grid @grid))