(ns health-it-exercise.android.utils
  (:require [clojure.string]))

(defn patient->string [patient]
  (let [name (first (:name patient))]
    (cond
      (not-any? nil? [name (:text name)]) (:text name)
      (not-any? nil? [name (:given name) (seq (:given name))]) (clojure.string/join " " (:given name))
      (not-any? nil? [name (:family name) (seq (:family name))]) (clojure.string/join " " (:family name))
      :else "Unknown")))

(defn period->str [period]
  (str (:start period) " - " (:end period)))

(defn not-blank? [s]
  (not (clojure.string/blank? s)))

(defn vec-remove
  "remove elem in coll"
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))
