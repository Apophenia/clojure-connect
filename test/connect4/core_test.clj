(ns connect4.core-test
  (:require [clojure.test :refer :all]
            [connect4.core :refer :all]))

; TEST GRIDS
(def bottom-row-o-win (apply conj [[-1 -1 -1 -1 0 0 0]]
                                       (vec (repeat (dec num-rows) (vec (repeat num-columns 0))))))
(def bottom-row-x-win (apply conj [[1 1 1 1 0 0 0]]
                                       (vec (repeat (dec num-rows) (vec (repeat num-columns 0))))))
(def second-row-o-win (apply conj [[-1 1 -1 1 -1 1 -1][1 1 1 -1 -1 -1 -1]]
                                       (vec (repeat (dec (dec num-rows)) (vec (repeat num-columns 0))))))
(def second-row-x-win (apply conj [[1 -1 1 -1 1 -1 -1][-1 -1 -1 1 1 1 1]]
                                       (vec (repeat (dec (dec num-rows)) (vec (repeat num-columns 0))))))
(def third-row-x-win (apply conj  [[1 1 -1 1 1 -1 1][-1 1 1 -1 1 1 -1][1 1 1 1 0 -1 -1 -1]]
                                       (vec (repeat (- num-rows 3)(vec (repeat num-columns 0))))))
(def fourth-row-o-win (apply conj [[1 1 -1 1 1 -1 1][-1 1 1 -1 1 1 -1][1 -1 -1 1 -1 -1 1][1 1 1 -1 -1 -1 -1]]
                                       (vec (repeat (- num-rows 4)(vec (repeat num-columns 0))))))
(def first-column-o-win (vec (apply conj (repeat 4 [-1 0 0 0 0 0 0]) (repeat 2 [0 0 0 0 0 0 0]))))
(def bottom-row-full (vec (conj (repeat 5 [0 0 0 0 0 0 0]) [1 1 1 1 1 1 1])))
(def top-row-full (conj (vec (repeat 5 [0 0 0 0 0 0 0])) (vec (repeat 7 1))))
(def alternating-rows (vec (map #(vec (repeat 7 %)) '(0 0 0 0 0 1))))

(def left-diagonal-x-win [[1 0 0 0 0 0 0]
                               [0 1 0 0 0 0 0]
                               [0 0 1 0 0 0 0]
                               [0 0 0 1 0 0 0]
                               [0 0 0 0 0 0 0]
                               [0 0 0 0 0 0 0]])
(def totally-random-impossible [[1 0 0 0 0 0 0]
                                     [0 -1 0 1 0 0 0]
                                     [0 0 1 0 0 0 0]
                                     [0 0 0 0 0 0 0]
                                     [1 0 0 0 0 0 0]
                                     [0 0 0 0 0 -1 0]])
(def almost-win [[-1 1 -1 1 -1 1 -1]
                 [-1 1 -1 1 -1 1 -1]
                 [-1 1 -1 1 -1 1 -1]
                 [0 -1 1 -1 1 -1 1]
                 [0 0 0 1 -1 0 0]
                 [0 0 0 0 0 0 0]])
(def almost-win-2 [[-1 1 -1 1 -1 1 -1]
                   [-1 1 -1 1 -1 1 -1]
                   [-1 1 -1 1 -1 1 -1]
                   [ 1 -1 1 -1 1 -1 1]
                   [-1 1 -1 1 -1 1 -1]
                   [0  0 -1 1  0  0 -1]])
(def almost-win-top-empty [[-1 1 -1 1 -1 1 -1]
                           [-1 1 -1 1 -1 1 -1]
                           [-1 1 -1 1 -1 1 -1]
                           [ 1 -1 1 -1 1 -1 1]
                           [-1 1 -1 1 -1 1 -1]
                           [0  0 0 0 0 0 -1]])
(def tied-board [[-1 1 -1 1 -1 1 -1]
                           [-1 1 -1 1 -1 1 -1]
                           [-1 1 -1 1 -1 1 -1]
                           [ 1 -1 1 -1 1 -1 1]
                           [-1 1 -1 1 -1 1 -1]
                           [-1 1 -1 1 -1 1 -1]])

(defn minimax-win [starting-grid current-player other-player]
  (let [best-move (minimax starting-grid current-player other-player),
        result-grid (drop-piece starting-grid best-move (:value current-player))]
    (do (draw-grid starting-grid)
     (draw-grid result-grid)
     (and (not (win-check starting-grid)) (win-check result-grid)))))

(defn partial-board [n]
  (let [empty-row-n (long (/ n 7))
        empty-pieces-n (mod n 7)
        board (take (- 6 (inc empty-row-n)) tied-board)
        partial-row (concat (take (- 7 empty-pieces-n)
                                  (nth tied-board (inc empty-row-n)))
                            (repeat empty-pieces-n 0))]
    (into [] (concat board
                     (vector (vec partial-row)) ;; ... :<
                     (repeat empty-row-n (vec (repeat 7 0)))))))

(deftest win-test
  (testing "testing everything."
    (is (= (win-check bottom-row-o-win) -1))
    (is (= (win-check bottom-row-x-win) 1))
    (is (= (win-check second-row-o-win) -1))
   (doseq [x (range 1 11)]
        (println "now running with" x "spaces open:")
         (time (is (minimax-win (partial-board x) denis joe))))))
