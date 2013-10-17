(ns connect4.core
  (:gen-class))

(def string-map
 {1 "X", -1 "O", 0 "_" :left-pipe "|" :right-pipe "|"})

(def num-rows 6)

(def num-columns 7)

(def grid (atom (vec (repeat num-rows (vec (repeat num-columns 1))))))

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
    (cond (= -1 y) (throw (Exception. "Passed a non-playable column to drop-piece."))
          (zero? (get-cell-value [x y])) (set-cell-value current-grid [x y] value)
          :else (recur (dec y)))))

(defn column-open?
  [current-grid x]
  (zero? (get-cell-value @current-grid [x 0])))

(defn -main [& args]
  (draw-grid @grid)
  (println (get-cell-value @grid [2 2]))
  (set-cell-value grid [2 2] 0)
  (println (get-cell-value @grid [2 2]))
  (draw-grid @grid))