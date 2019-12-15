(ns plzwork.views.reflist
  (:require [re-frame.core :as rf]
            [plzwork.subs :as subs]))


(defn tags-list []
  (let [tags @(rf/subscribe [::subs/tags])]
   [:div.tags-list
    (doall
     (for [tag tags]
       ^{:key tag}
       [:h4.tag tag]))]))

(defn recent-tags-list []
  (let [tags @(rf/subscribe [::subs/recent-tags])]
    [:div.recent-tags-list
     (doall
      (for [tag tags]
        ^{:key tag}
        [:h4.tag tag]))]))




(defn ref-card [index ref-meta]
  [:div.ref-card
   [:h2.ref-index (if (< index 10) (str "0" index "/") (str index "/"))]
   [:h2.ref-title (:title ref-meta)]
   [:ul.ref-tags-list
    (doall 
     (for [tag (:tags ref-meta)]
       ^{:key (:id tag)}
       [:div.ref-tags [:h4.tag tag]]))]])

(defn ref-list []
  (let [refs @(rf/subscribe [::subs/refs])]
    [:div.ref-list
     (doall (map-indexed
             (fn [i ref]
               ^{:key (:id ref)}
               [ref-card (inc i) ref])
             refs))]))

(defn recent-cards-list []
  (let [cards @(rf/subscribe [::subs/recent-cards])]
    (doall (map-indexed
            (fn [i ref]
              ^{:key (:id ref)}
              [ref-card (inc i) ref])
            cards))))