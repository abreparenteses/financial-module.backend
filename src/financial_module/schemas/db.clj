(ns financial-module.schemas.db
  (:require [schema.core :as s]))

(def accounts-payable {:accounts_payable/id s/Uuid
                       :accounts_payable/removed s/Bool
                       :accounts_payable/name s/Str
                       :accounts_payable/description s/Str
                       :accounts_payable/amount s/Num
                       :accounts_payable/created_at s/Inst})

(s/defschema AccountsPayableTransaction
  (select-keys accounts-payable [:accounts_payable/id
                                 :accounts_payable/removed
                                 :accounts_payable/name
                                 :accounts_payable/description
                                 :accounts_payable/amount]))

(s/defschema AccountsPayableEntry
  (select-keys accounts-payable [:accounts_payable/id
                                 :accounts_payable/name
                                 :accounts_payable/description
                                 :accounts_payable/amount
                                 :accounts_payable/created_at]))
