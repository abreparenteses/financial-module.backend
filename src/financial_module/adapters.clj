(ns financial-module.adapters
  (:require [financial-module.schemas.db :as schemas.db]
            [financial-module.schemas.types :as schemas.types]
            [financial-module.schemas.wire-in :as schemas.wire-in]
            [financial-module.schemas.wire-out :as schemas.wire-out]
            [schema.core :as s])
  (:import [java.time LocalDateTime ZoneId]
           [java.time.format DateTimeFormatter]))

(s/defn ^:private date->localdatetime :- LocalDateTime
  [value :- s/Inst
   zone-id :- ZoneId]
  (-> value
      (.toInstant)
      (.atZone zone-id)
      (.toLocalDateTime)))

(s/defn inst->utc-formated-string :- s/Str
  [inst :- s/Inst
   str-format :- s/Str]
  (-> inst
      (date->localdatetime (ZoneId/of "UTC"))
      (.format (DateTimeFormatter/ofPattern str-format))))

(s/defn db->wire-in :- schemas.wire-in/AccountsPayableEntry
  [{:accounts_payable/keys [id name description amount created_at]} :- schemas.db/AccountsPayableEntry]
  {:id id
   :name name
   :description description
   :amount (bigdec amount)
   :created-at created_at})

(s/defn ->accounts-payable-history :- schemas.wire-in/AccountsPayableHistory
  [accounts-payable-entries :- [schemas.db/AccountsPayableEntry]]
  (let [total (reduce #(+ (:accounts_payable/amount %2) %1) 0M accounts-payable-entries)]
    {:entries (mapv (fn [{:accounts_payable/keys [id name description amount created_at]}]
                      {:id id
                       :name name
                       :description description
                       :amount (bigdec amount)
                       :created-at created_at})
                    accounts-payable-entries)
     :total (bigdec total)}))

(s/defn ->accounts-payable :- schemas.wire-in/AccountsPayableEntry
  [accounts-payable-entries :- [schemas.db/AccountsPayableEntry]]
  (let [account-payable-entry (first accounts-payable-entries)]
    (db->wire-in account-payable-entry)))
