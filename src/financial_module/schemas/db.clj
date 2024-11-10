(ns financial-module.schemas.db
  (:require [schema.core :as s]))

(def accounts-payable {:accounts-payable/id s/Uuid
                       :accounts-payable/removed s/Bool
                       :accounts-payable/name s/Str
                       :accounts-payable/description s/Str
                       :accounts-payable/amount s/Num
                       :accounts-payable/created_at s/Inst})

(s/defschema AccountsPayableTransaction
  (select-keys accounts-payable [:accounts-payable/id
                                 :accounts-payable/removed
                                 :accounts-payable/name
                                 :accounts-payable/description
                                 :accounts-payable/amount]))

(s/defschema AccountsPayableEntry
  (select-keys accounts-payable [:accounts-payable/id
                                 :accounts-payable/removed
                                 :accounts-payable/name
                                 :accounts-payable/description
                                 :accounts-payable/amount
                                 :accounts-payable/created_at]))
