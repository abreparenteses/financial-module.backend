(ns financial-module.logics
  (:require [financial-module.adapters :as adapters]
            [financial-module.schemas.db :as schemas.db]
            [schema.core :as s])
  (:import [java.util UUID]))

(s/defn uuid-from-string :- s/Uuid
  [seed :- s/Str]
  (-> seed
      .getBytes
      UUID/nameUUIDFromBytes))

(s/defn uuid-from-date-amount :- s/Uuid
  [date :- s/Inst
   amount :- s/Num]
  (-> date
      (adapters/inst->utc-formated-string "yyyy-MM-dd hh:mm:ss")
      (str amount)
      uuid-from-string))

(s/defn ->accounts-payable-transaction :- schemas.db/AccountsPayableTransaction
  [date :- s/Inst
   name :- s/Str
   description :- s/Str
   amount :- s/Num]
  #:accounts_payable{:id (uuid-from-date-amount date amount)
                     :removed false
                     :name name
                     :description description
                     :amount amount})

(s/defn ->accounts-payable-update-transaction :- schemas.db/AccountsPayableTransaction
  [id :- s/Uuid
   name :- s/Str
   description :- s/Str
   amount :- s/Num]
  #:accounts_payable{:id id
                     :removed false
                     :name name
                     :description description
                     :amount amount})
