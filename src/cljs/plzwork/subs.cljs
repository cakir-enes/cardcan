(ns plzwork.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 ::name
 (fn [db]
   (:name db)))

(rf/reg-sub
 ::recent-tags
 (fn [db]
   (:recent-tags db)))

(rf/reg-sub
 ::recent-cards
 (fn [db]
   (:recent-cards db)))

(rf/reg-sub
 ::all-tags
 (fn [db]
   (:all-tags db)))

(rf/reg-sub
 ::card-meta
 (fn [db]
   (:card-meta db)))

(rf/reg-sub
 ::refs
 (fn [db]
   (let [refs (-> db :card-meta :refs)]
     (println "Active: " (:active refs))
     ((:active refs) refs))))

(rf/reg-sub
 ::spotlight?
 (fn [db]
   (println "SPOT CAHGNEd " (:spotlight? db))
   (:spotlight? db)))