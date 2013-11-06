(ns connect4.core-test
  (:require [clojure.test :refer :all]
            [connect4.core :refer :all]))

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
(def test-first-column-o-win (vec (apply conj (repeat 4 [-1 0 0 0 0 0 0]) (repeat 2 [0 0 0 0 0 0 0]))))
(def test-bottom-row-full (vec (conj (repeat 5 [0 0 0 0 0 0 0]) [1 1 1 1 1 1 1]))) 
(def test-top-row-full (conj (vec (repeat 5 [0 0 0 0 0 0 0])) (vec (repeat 7 1))))
(def alternating-rows (vec (map #(vec (repeat 7 %)) '(0 0 0 0 0 1))))


(def test-left-diagonal-x-win [[1 0 0 0 0 0 0]
                               [0 1 0 0 0 0 0]
                               [0 0 1 0 0 0 0]
                               [0 0 0 1 0 0 0]
                               [0 0 0 0 0 0 0]
                               [0 0 0 0 0 0 0]])
(def test-totally-random-impossible [[1 0 0 0 0 0 0]
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

(deftest horizontal-win-test
  (testing "Testing for a horizontal win."
    (is (win-check test-bottom-row-o-win))
    (is (win-check test-bottom-row-o-win))
    (is (win-check test-bottom-row-o-win))
    (is (win-check test-bottom-row-x-win))
    (is (win-check test-second-row-x-win))
    (is (win-check test-second-row-o-win))
    (is (win-check test-third-row-x-win))
    (is (win-check test-fourth-row-o-win))
    (is (win-check test-first-column-o-win))
    (is (win-check test-left-diagonal-x-win))
    (is (not (win-check test-totally-random-impossible)))
    (is (not (column-open? test-first-column-o-win 0)))
    (is (column-open? test-bottom-row-full 0))
    (is (not (tie? test-bottom-row-full)))
    (is (tie? test-top-row-full))
 ;;   (is (= 4 (minimax almost-win-2 denis joe)))
 ;;   (println (minimax almost-win-2 denis joe))
    (do (draw-grid almost-win-top-empty)
        (is (= 4 (minimax almost-win-top-empty denis joe))))
))
