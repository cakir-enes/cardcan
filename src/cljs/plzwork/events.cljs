(ns plzwork.events
  (:require
   [re-frame.core :as rf]
   [plzwork.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(rf/reg-event-db
 ::update-last-edit
 (fn [db _]
   (assoc-in db [:card-meta :last-edited] (new js/Date))))

(rf/reg-event-db
 ::set-tags
 (fn [db [_ new-tags]]
   (assoc-in db [:card-meta :tags] new-tags)))

(rf/reg-event-fx
 ::ref-card
 (fn [{:keys [db]} [_ card-meta]]
   {:db (assoc-in db [:card-meta :refs :to] (:id card-meta))
    :update-reffed-card {:unref? false :from (-> db :card-meta :id) :to (:id card-meta)}}))

(rf/reg-event-fx
 ::deref-card
 (fn [{:keys [db]} [_ card-meta]]
   {:db (update-in db [:card-meta :refs :to] #(filter (partial not= (:id card-meta))))
    :update-reffed-card {:unref? true :from (-> db :card-meta :id) :to (:id card-meta)}}))


(rf/reg-fx
 :update-reffed-card
 (fn [{:keys [unref? from to]}]
   (println (if unref? "REF-DELETED: " "REF-ADDED: ") from "->" to)))