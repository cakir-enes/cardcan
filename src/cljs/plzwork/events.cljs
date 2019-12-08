(ns plzwork.events
  (:require
   [re-frame.core :as rf]
   [plzwork.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [plzwork.editor :as e]
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

(rf/reg-event-fx
 ::shortcut-pressed
 (fn [{:keys [db]} [_ shortcut]]
   (case shortcut
     :ESC {:db (assoc db :spotlight? false)
           :focus-editor nil}
     :CTRL_SPC (let [s (:spotlight? db)]
                 (if s {:db (assoc db :spotlight? false)} {:db (assoc db :spotlight? true) :focus-spotlight nil})))))

(rf/reg-event-fx
 ::open-spotlight
 (fn [{:keys [db]} [_]]
   {:db (assoc db :spotlight? true)
    :focus-spotlight nil}))

(rf/reg-event-db
 ::close-spotlight
 (fn [db [_]]
   (assoc db :spotlight? false)))

(rf/reg-event-db
 ::toggle-ref-list
 (fn [db [_]]
   (update-in db [:card-meta :refs :active] #(if (= % :to) :from :to))))

(rf/reg-event-fx
 ::focus-editor
 (fn [_ _]
   {:focus-editor nil}))

(rf/reg-event-fx 
 ::cmd-invoked
 (fn [{:keys [db]} [_ cmd]]
   (let [update-center #(assoc db :center-content %)]
     (case (:id cmd)
       :open-cards {:db (update-center :all-cards)}
       :open-inbox {:db (update-center :inbox-card)}
       :open-todos {:db (update-center :todos-card)}
       :open-ref-dialog  {:db (update-center :ref-dialog)}
       :ref-page {:ref-current-page nil}))))

(rf/reg-fx
 :update-reffed-card
 (fn [{:keys [unref? from to]}]
   (println (if unref? "REF-DELETED: " "REF-ADDED: ") from "->" to)))

(rf/reg-fx
 :ref-current-page
 (fn []
   (println "REF CURR PAGE FIRED")))

(rf/reg-fx
 :focus-editor
 (fn []
   (.focus @e/editor true)))

(rf/reg-fx
 :focus-spotlight
 (fn []
   (.focus (.getElementById js/document "cmd-txt"))))