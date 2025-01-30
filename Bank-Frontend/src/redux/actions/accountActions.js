import * as actionTypes from "./actionTypes";
import apiClient from "../../apiClient";  // Import apiClient

// Action creators
export function changeAccount(account) {
  return { type: actionTypes.CHANGE_ACCOUNT, payload: account };
}

export function getAccountsSuccess(accounts) {
  return { type: actionTypes.GET_ACCOUNTS_SUCCESS, payload: accounts };
}

export function getTotalBalanceSuccess(balance) {
  return { type: actionTypes.GET_TOTAL_BALANCE_SUCCESS, payload: balance };
}

export function getTransactionHistorySuccess(history) {
  return { type: actionTypes.GET_TRANSACTION_HISTORY_SUCCESS, payload: history };
}

// Thunks that use apiClient
export function getAccounts() {
  return async (dispatch) => {
    try {
      const response = await apiClient.get("/app/dashboard");  // API endpoint
      dispatch(getAccountsSuccess(response.data.userAccounts));  // Dispatch action
    } catch (error) {
      console.error("Error fetching accounts:", error);
    }
  };
}

export function getTotalBalance() {
  return async (dispatch) => {
    try {
      const response = await apiClient.get("/app/dashboard");  // API endpoint
      dispatch(getTotalBalanceSuccess(response.data.totalBalance));  // Dispatch action
    } catch (error) {
      console.error("Error fetching total balance:", error);
    }
  };
}

export function getTransactionHistory() {
  return async (dispatch) => {
    try {
      const response = await apiClient.get("/app/transaction_history");  // API endpoint
      dispatch(getTransactionHistorySuccess(response.data.transaction_history));  // Dispatch action
    } catch (error) {
      console.error("Error fetching transaction history:", error);
    }
  };
}
