(ns connect4.core
  (:gen-class))

; TO-DO/TO-LEARN
; MACROS: Clojure Programming p. 232 (chapter 5)
; TALK TO ZACH ABOUT: PROTOCOLS because really, ATOMIC VS NON-ATOMIC BOARD, GAMES AND CONCURRENCY,
; GETTING BETTER AT READING ERRORS/FINDING LINE NUMBERS

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
   (swap! current-grid assoc-in [y x] value))

(defn drop-piece 
  [current-grid x value]
  (loop [y 0]
    (cond (>= y num-rows) (throw (Exception. "Passed a non-playable row to drop-piece."))
          (zero? (get-cell-value @current-grid [x y])) (set-cell-value current-grid [x y] value)
          :else (recur (inc y)))))

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
  ; (loop [current-player player-one
  ;        other-player player-two]
    (drop-piece grid (make-move player-one @grid) (:value player-one))
    (draw-grid @grid)
    (drop-piece grid (make-move player-two @grid) (:value player-two))
    (draw-grid @grid)
    ; (if (game-over? @grid) 
    ;   (println "The game is over")
    ; (recur other-player current-player)))
    )

(defn -main [& args]
  ; (drop-piece @grid 0 -1)
  ;(set-cell-value grid [0 0] -1)
  (draw-grid @grid)
   (let [human (HumanPlayer. "Lyndsey" 1)
         computer (ComputerPlayer. -1)]
   (game-loop human computer))
)

