(ns health-it-exercise.android.add-patient-screen
  (:require [reagent.core :as r :refer [atom]]
            [goog.date.Date]
            [health-it-exercise.android.ui :as ui]
            [health-it-exercise.android.utils :as utils]
            [health-it-exercise.android.patients-screen :as patients-screen]
            [health-it-exercise.android.api :as api]))

(def names [])
(def use-field (atom ""))
(def text-field nil)
(def family-field nil)
(def given [])
(def given-name nil)
(def prefix [])
(def prefix-name nil)
(def suffix [])
(def suffix-name nil)
(def period-start (atom ""))
(def period-end (atom ""))

(def contact-points [])
(def system-field (atom ""))
(def value-filed nil)
(def cp-use-filed (atom ""))
(def rank nil)
(def cp-period-start (atom ""))
(def cp-period-end (atom ""))

(def gender-field (atom ""))
(def birthdate (atom ""))

(defn clear-patient-name []
  (reset! use-field "")
  (set! text-field nil)
  (set! family-field nil)
  (set! given [])
  (set! given-name nil)
  (set! prefix [])
  (set! prefix-name nil)
  (set! suffix [])
  (set! suffix-name nil)
  (reset! period-start "")
  (reset! period-end ""))

(defn clear-contact-point []
  (reset! system-field "")
  (set! value-filed nil)
  (reset! cp-use-filed "")
  (set! rank nil)
  (reset! cp-period-start "")
  (reset! cp-period-end ""))

(defn clear-patient []
  (set! names [])
  (clear-patient-name)

  (set! contact-points [])
  (clear-contact-point)

  (reset! gender-field "")
  (reset! birthdate ""))

(defn add-patient [go-back]
  (let [patient (merge {:resourceType "Patient"}
                       (when (seq names)
                         {:name names})
                       (when (seq contact-points)
                         {:telecom contact-points})
                       (when (utils/not-blank? @gender-field)
                         {:gender @gender-field})
                       (when (utils/not-blank? @birthdate)
                         {:birthDate @birthdate}))]
    (api/create-patient patient
                        #(do
                           (patients-screen/add-row patient)
                           (clear-patient)
                           (go-back nil))
                        #(ui/alert "Add patient" "Add patient error"))))

(defn patient-names [parent]
  (for [[i name] (map-indexed #(vector %1 %2) names)]
    ^{:key i}
    [ui/view
     (ui/name-info name)
     [ui/button {:title "Remove Name" :on-press (fn [] (set! names (vec (utils/vec-remove names i))) (r/force-update parent))}]]))

(defn given-names [parent]
  (for [[i item] (map-indexed #(vector %1 %2) given)]
    ^{:key i}
    [ui/view
     [ui/text (str item)]
     [ui/button {:title "Remove given name" :on-press (fn [] (set! given (utils/vec-remove given i)) (r/force-update parent))}]]))

(defn prefix-names [parent]
  (for [[i item] (map-indexed #(vector %1 %2) prefix)]
    ^{:key i}
    [ui/view
     [ui/text (str item)]
     [ui/button {:title "Remove prefix" :on-press (fn [] (set! prefix (utils/vec-remove prefix i)) (r/force-update parent))}]]))

(defn suffix-names [parent]
  (for [[i item] (map-indexed #(vector %1 %2) suffix)]
    ^{:key i}
    [ui/view
     [ui/text (str item)]
     [ui/button {:title "Remove suffix" :on-press (fn [] (set! suffix (utils/vec-remove suffix i)) (r/force-update parent))}]]))

(defn check-period []
  (let [start (if (empty? @period-start) nil (js/Date. @period-start))
        end (if (empty? @period-end) nil (js/Date. @period-end))]
    (cond
      (and (nil? start) (not (nil? end)))
      (swap! period-start (fn [prev] @period-end))

      (not-any? nil? [start end])
      (when (> (goog.date.Date/compare start end) 0)
        (swap! period-end (fn [prev] @period-start))))))

(defn check-cp-period []
  (let [start (if (empty? @cp-period-start) nil (js/Date. @cp-period-start))
        end (if (empty? @cp-period-end) nil (js/Date. @cp-period-end))]
    (cond
      (and (nil? start) (not (nil? end)))
      (swap! cp-period-start (fn [prev] @cp-period-end))

      (not-any? nil? [start end])
      (when (> (goog.date.Date/compare start end) 0)
        (swap! cp-period-end (fn [prev] @cp-period-start))))))

(defn add-name [comp]
  (let [name (merge {}
                    (when (utils/not-blank? @use-field)
                      {:use @use-field})
                    (when (utils/not-blank? text-field)
                      {:text text-field})
                    (when (utils/not-blank? family-field)
                      {:family [family-field]})
                    (when (seq given)
                      {:given given})
                    (when (seq prefix)
                      {:prefix prefix})
                    (when (seq suffix)
                      {:suffix suffix})
                    (when (utils/not-blank? @period-start)
                      {:period
                       (merge {:start @period-start}
                              (when (utils/not-blank? @period-end)
                                {:end @period-end}))}))]
    (set! names (conj names name))
    (clear-patient-name)
    (r/force-update comp)))

(defn add-contact-point [comp]
  (let [contact-point (merge {}
                             (when (utils/not-blank? @system-field)
                               {:system @system-field})
                             (when (utils/not-blank? value-filed)
                               {:value value-filed})
                             (when (utils/not-blank? @cp-use-filed)
                               {:use @cp-use-filed})
                             (when (utils/not-blank? rank)
                               {:rank (js/parseInt rank)})
                             (when (utils/not-blank? @cp-period-start)
                               {:period
                                (merge {:start @cp-period-start}
                                       (when (utils/not-blank? @cp-period-end)
                                         {:end @cp-period-end}))}))]
    (set! contact-points (conj contact-points contact-point))
    (clear-contact-point)
    (r/force-update comp)))

(defn patient-contact-points [parent]
  (for [[i point] (map-indexed #(vector %1 %2) contact-points)]
    ^{:key i}
    [ui/view
     (ui/cp-info point)
     [ui/button {:title "Remove Contact Point" :on-press (fn [] (set! contact-points (vec (utils/vec-remove contact-points i))) (r/force-update parent))}]]))

(defn add-patient-screen [props]
  (let [{:keys [navigation]} props
        go-back (.-goBack navigation)
        this (r/current-component)]
    (fn []
      [ui/view
       [ui/scroll-view
        [ui/button {:title "Go Back" :on-press #(do (clear-patient) (go-back nil))}]
        [ui/text {:style {:height 10}}]
        [ui/button {:title "Add" :on-press #(add-patient go-back)}]

        [ui/text {:style {:font-size 20 :font-weight "bold"}} "Names"]
        (patient-names this)
        
        [ui/text {:style {:margin-top 20 :font-size 20 :font-weight "bold"}} "New name properties"]
        [ui/text "Purpose of this name"]
        [ui/picker {:selected-value @use-field :on-value-change (fn [v] (swap! use-field (fn [prev] v)))}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Usual" :value "usual"}]
         [ui/picker-item {:label "Official" :value "official"}]
         [ui/picker-item {:label "Temporary" :value "temp"}]
         [ui/picker-item {:label "Nickname" :value "nickname"}]
         [ui/picker-item {:label "Anonymous" :value "anonymous"}]
         [ui/picker-item {:label "Old" :value "old"}]
         [ui/picker-item {:label "Maiden" :value "maiden"}]]
        
        [ui/text "Text representation of the name"]
        [ui/input {:on-change-text (fn [text] (set! text-field text))}]
        
        [ui/text "Family name (often called 'Surname')"]
        [ui/input {:on-change-text (fn [text] (set! family-field text))}]

        [ui/text "Given names"]
        (given-names this)

        [ui/text "Given name"]
        [ui/input {:on-change-text (fn [text] (set! given-name text))}]
        [ui/button {:title "Add given name" :on-press (fn [] (when-not (nil? given-name) (set! given (conj given given-name))) (r/force-update this))}]
        
        [ui/text "Parts that come before the name"]
        (prefix-names this)

        [ui/text "Prefix"]
        [ui/input {:on-change-text (fn [text] (set! prefix-name text))}]
        [ui/button {:title "Add prefix" :on-press (fn [] (when-not (nil? prefix-name) (set! prefix (conj prefix prefix-name))) (r/force-update this))}]
        
        [ui/text "Parts that come after the name"]
        (suffix-names this)

        [ui/text "Suffix"]
        [ui/input {:on-change-text (fn [text] (set! suffix-name text))}]
        [ui/button {:title "Add suffix" :on-press (fn [] (when-not (nil? suffix-name) (set! suffix (conj suffix suffix-name))) (r/force-update this))}]

        [ui/text "Period Start"]
        [ui/date-picker {:placeholder "Select Date" :date @period-start :on-date-change (fn [date] (swap! period-start (fn [prev] date)) (check-period))}]
        [ui/button {:title "Clear Date" :on-press (fn [] (reset! period-start "") (check-period))}]

        [ui/text "Period End"]
        [ui/date-picker {:placeholder "Select Date" :date @period-end :on-date-change (fn [date] (swap! period-end (fn [prev] date)) (check-period))}]
        [ui/button {:title "Clear Date" :on-press (fn [] (reset! period-end "") (check-period))}]

        [ui/button {:title "Add Name" :on-press #(add-name this)}]

        [ui/text {:style {:font-size 20 :font-weight "bold"}} "Contact Points"]
        (patient-contact-points this)

        [ui/text {:style {:margin-top 20 :font-size 20 :font-weight "bold"}} "New contact properties"]
        [ui/text "System"]
        [ui/picker {:selected-value @system-field :on-value-change (fn [v] (swap! system-field (fn [prev] v)))}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Phone" :value "phone"}]
         [ui/picker-item {:label "Fax" :value "fax"}]
         [ui/picker-item {:label "Email" :value "email"}]
         [ui/picker-item {:label "Pager" :value "pager"}]
         [ui/picker-item {:label "URL" :value "url"}]
         [ui/picker-item {:label "SMS" :value "sms"}]
         [ui/picker-item {:label "other" :value "other"}]]
        
        [ui/text "Value"]
        [ui/input {:on-change-text (fn [text] (set! value-filed text))}]
        
        [ui/text "Use"]
        [ui/picker {:selected-value @cp-use-filed :on-value-change (fn [v] (swap! cp-use-filed (fn [prev] v)))}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Home" :value "home"}]
         [ui/picker-item {:label "Work" :value "work"}]
         [ui/picker-item {:label "Temp" :value "temp"}]
         [ui/picker-item {:label "Old" :value "old"}]
         [ui/picker-item {:label "mobile" :value "mobile"}]]
        
        [ui/text "Rank"]
        [ui/input {:on-change-text (fn [text] (set! rank (cond (pos? (js/parseInt text)) (str (js/parseInt text)) (empty? text) "" :else "1")) (r/force-update this))
                   :value rank}]

        [ui/text "Period Start"]
        [ui/date-picker {:placeholder "Select Date" :date @cp-period-start :on-date-change (fn [date] (swap! cp-period-start (fn [prev] date)) (check-cp-period))}]
        [ui/button {:title "Clear Date" :on-press (fn [] (reset! cp-period-start "") (check-cp-period))}]

        [ui/text "Period End"]
        [ui/date-picker {:placeholder "Select Date" :date @cp-period-end :on-date-change (fn [date] (swap! cp-period-end (fn [prev] date)) (check-cp-period))}]
        [ui/button {:title "Clear Date" :on-press (fn [] (reset! cp-period-end "") (check-cp-period))}]

        [ui/button {:title "Add Contact Point" :on-press #(add-contact-point this)}]

        [ui/text "Gender"]
        [ui/picker {:selected-value @gender-field :on-value-change (fn [v] (swap! gender-field (fn [prev] v)))}
         [ui/picker-item {:label "" :value ""}]
         [ui/picker-item {:label "Male" :value "male"}]
         [ui/picker-item {:label "Female" :value "female"}]
         [ui/picker-item {:label "Other" :value "other"}]
         [ui/picker-item {:label "Unknown" :value "unknown"}]]

        [ui/text "Birthdate"]
        [ui/date-picker {:placeholder "Select Date" :date @birthdate :on-date-change (fn [date] (swap! birthdate (fn [prev] date)))}]
        [ui/button {:title "Clear Date" :on-press (fn [] (reset! birthdate ""))}]]])))
