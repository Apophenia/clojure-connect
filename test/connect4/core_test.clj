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
(def test-first-column-o-win (apply conj (repeat 4 [-1 0 0 0 0 0 0]) (repeat 2 [0 0 0 0 0 0 0])))


(deftest horizontal-win-test
  (testing "Testing for a horizontal win."
    (assert (horizontal-win test-bottom-row-o-win [1 0] -1))
    (assert (horizontal-win test-bottom-row-o-win [0 0] -1))
    (assert (horizontal-win test-bottom-row-o-win [1 0] -1))
    (assert (horizontal-win test-bottom-row-x-win [0 0] 1))
    (assert (horizontal-win test-second-row-x-win [6 1] 1))
    (assert (horizontal-win test-second-row-o-win [5 1] -1))
    (assert (horizontal-win test-third-row-x-win [3 2] 1))
    (assert (horizontal-win test-fourth-row-o-win [4 3] -1))
    (assert (vertical-win test-first-column-o-win [0, 0] -1))))
