(ns plzwork.db)

; (def default-db
;   {:name "re-frame"
;    :recent-tags []
;    :all-tags []
;    :recent-cards []
;    :card-meta {}})

(def default-db
  {:name "re-frame"
   :recent-tags ["programming/clojure" "databases/inmemory"]
   :all-tags ["programming/clojure" "databases/inmemory" "cats" "ideas/shit"]
   :recent-cards []
   :card-meta {:last-edited (.now js/Date)
               :tags        ["programming/swift" "nice"]
               :refs        {:to [{:id "SOMEID"}] :from [{:id "ANTERID"}]}}})
