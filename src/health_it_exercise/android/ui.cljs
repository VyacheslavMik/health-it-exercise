(ns health-it-exercise.android.ui
  (:require [reagent.core :as r]
            [health-it-exercise.android.utils :as utils]))

(def ReactNative (js/require "react-native"))
(def react-datepicker (js/require "react-native-datepicker"))

(def view (r/adapt-react-class (.-View ReactNative)))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def input (r/adapt-react-class (.-TextInput ReactNative)))
(def button (r/adapt-react-class (.-Button ReactNative)))
(def date-picker (r/adapt-react-class (aget react-datepicker "default")))
(def list-view (r/adapt-react-class (.-ListView ReactNative)))
(def picker (r/adapt-react-class (.-Picker ReactNative)))
(def picker-item (r/adapt-react-class (.-Picker.Item ReactNative)))
(def scroll-view (r/adapt-react-class (.-ScrollView ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(defn alert [title msg]
  (.alert (.-Alert ReactNative) title msg))

(defn name-info [name]
  [view
   [text (str "Purpose of this name: " (:use name))]
   [text (str "Text representation of the name: " (:text name))]
   [text (str "Family name (often called 'Surname'): " (first (:family name)))]
   [text (str "Given names: " (clojure.string/join " " (:given name)))]
   [text (str "Parts that come before the name: " (clojure.string/join " " (:prefix name)))]
   [text (str "Parts that come after the name: " (clojure.string/join " " (:suffix name)))]
   [text (str "Period: " (utils/period->str (:period name {})))]])

(defn cp-info [point]
  [view
   [text (str "System: " (:system point))]
   [text (str "Value: " (:value point))]
   [text (str "Purpose of this contact point: " (:use point))]
   [text (str "Preferred order of use: " (:rank point))]
   [text (str "Period: " (utils/period->str (:period point {})))]])
