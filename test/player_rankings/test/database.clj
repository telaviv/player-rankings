(ns player-rankings.test.database
  (:use clojure.test
        player-rankings.logic.database))

(deftest test-normalize-name
  (testing "removes team names with a |"
    (is (= "legit" (normalize-name "BKO | Legit" ["bko"]))))
  (testing "removes team names where l is the seperator."
    (is (= "egit" (normalize-name "BKO Legit" ["bko"]))))
  (testing "removes multiple names"
    (is (= "stark" (normalize-name "FONC | GPL Stark" ["fonc" "gpl"])))
    (is (= "nitro" (normalize-name "MADE| 1UP Nitro" ["made" "1up"]))))
  (testing "bars are always removed even without team names."
    (is (= "djrelly" (normalize-name "ST | DjRelly" [])))))
