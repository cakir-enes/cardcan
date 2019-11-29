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