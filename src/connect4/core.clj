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
                                       (vec (repeat (dec (dec num-rows)) (vec (repeat num-columns 0))))))
(def test-second-row-o-win (apply conj [[-1 1 -1 1 -1 1 -1][1 1 1 -1 -1 -1 -1]] 
                                       (vec (repeat (dec (dec num-rows)) (vec (repeat num-columns 0))))))
(def test-second-row-x-win (apply conj [[1 -1 1 -1 1 -1 -1][-1 -1 -1 1 1 1 1]] 
                                       (vec (repeat (dec (dec num-rows)) (vec (repeat num-columns 0))))))
(def test-third-row-x-win (apply conj  [[1 1 -1 1 1 -1 1][-1 1 1 -1 1 1 -1][1 1 1 1 0 -1 -1 -1]]
                                       (vec (repeat (- num-rows 3)(vec (repeat num-columns 0))))))
(def test-fourth-row-o-win (apply conj [[1 1 -1 1 1 -1 1][-1 1 1 -1 1 1 -1][1 -1 -1 1 -1 -1 1]]
                                       (vec (repeat (- num-rows 4)(vec (repeat num-columns 0))))))
(defn get-cell-value 
  [current-grid [x y]]
  ((current-grid y) x))

(defn get-column [current-grid x]
  (vec (map #(% x) current-grid)))

(defn win-check [current-grid]
  (cond (every? zero? (current-grid 3)) )

; (def row [0 1 0 0 0 0 0])
; (every? zero? row)

(defn draw-cells 
  [row i]
  (if (not= i num-columns)
    (do (print (string-map :left-pipe) (string-map (row i)))
      (recur row (inc i)))
    (print (string-map :right-pipe))))

(defn draw-row
  [current-grid n]
 (if (<= 0 n) 
  (do
      (draw-cells (current-grid n) 0)
      (println)
      (recur current-grid (dec n)))))

(defn draw-grid
  [current-grid]
  (draw-row current-grid (dec num-rows)))

(defn set-cell-value
  [current-grid [x y] value]
   (assoc-in current-grid [y x] value))

(defn drop-piece 
  [current-grid x value]
  (loop [y 0]
    (cond (>= y num-rows) (throw (Exception. (str "Passed " y " to function")))
          (zero? (get-cell-value current-grid [x y])) (set-cell-value current-grid [x y] value)
          :else (recur (inc y)))))

(defn column-open?
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

(defn game-loop [current-grid current-player other-player]
  (let [next-grid (drop-piece current-grid (make-move current-player current-grid) (:value current-player))]
   (draw-grid next-grid)
   ;(if (game-over? @grid) 
   ;    (println "The game is over")
    (recur next-grid other-player current-player)))

(defn -main [& args]
  ; (drop-piece @grid 0 -1)
  ;(set-cell-value grid [0 0] -1)

  (println test-grid-one)
  (draw-grid test-grid-one)
  (println (get-column test-grid-one 1))
   (let [human (HumanPlayer. "Lyndsey" 1)
         computer (ComputerPlayer. -1)]
        (game-loop initial-grid human computer)))

; TEST DRIVEN DEVELOPMENT, GUYS

