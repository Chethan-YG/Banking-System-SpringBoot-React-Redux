import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Title from "./Title";
import { useEffect, useState } from "react";
import { connect } from "react-redux";
import Button from "@mui/material/Button";
import { bindActionCreators } from "redux";
import * as accountActions from "../../redux/actions/accountActions";

function TransactionHistory(props) {
  const [showAllAccounts, setShowAllAccounts] = useState(false);

  useEffect(() => {
    props.getTransactionHistory();
  }, [props.getTransactionHistory]);

  function dateFormatter(dateString) {
    const date = new Date(dateString);  
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0'); 
    const day = date.getDate().toString().padStart(2, '0'); 
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    const second = date.getSeconds().toString().padStart(2, '0');
  
    // Format as: YYYY : MM : DD HH:MM:SS
    return `${year} : ${month} : ${day} ${hour}:${minute}:${second}`;
  }
  

  // Check if transaction history exists, if not show a message
  if (!props.transactHistory || props.transactHistory.length === 0) {
    return <div>No transaction history available.</div>;
  }

  return (
    <React.Fragment>
      <Title>Transaction History</Title>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Transaction Id</TableCell>
            <TableCell>Account Id</TableCell>
            <TableCell>Transaction Type</TableCell>
            <TableCell>Amount</TableCell>
            <TableCell>Source</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Reason Code</TableCell>
            <TableCell>Created At</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.transactHistory && props.transactHistory.length > 0 ? (
            props.transactHistory
              .sort((a, b) => b.transactionId - a.transactionId)
              .map((history) => (
                <TableRow key={history.transactionId}>
                  <TableCell>{history.transactionId}</TableCell>
                  <TableCell>{history.accountId}</TableCell>
                  <TableCell>{history.transactionType}</TableCell>
                  <TableCell>{history.amount}</TableCell>
                  <TableCell>{history.source}</TableCell>
                  <TableCell>{history.status}</TableCell>
                  <TableCell>{history.reasonCode}</TableCell>
                  <TableCell>{dateFormatter(history.createdAt)}</TableCell>
                </TableRow>
              ))
          ) : (
            <TableRow>
              <TableCell colSpan={8} align="center">
                No transaction history available.
              </TableCell>
            </TableRow>
          )}
        </TableBody>
      </Table>
      <Button onClick={() => setShowAllAccounts(!showAllAccounts)}>
        {showAllAccounts ? "Show Less Accounts" : "Show All Accounts"}
      </Button>
    </React.Fragment>
  );
}

function mapDispatchToProps(dispatch) {
  return {
    getTransactionHistory: bindActionCreators(
      accountActions.getTransactionHistory,
      dispatch
    ),
  };
}

function mapStateToProps(state) {
  return {
    transactHistory: state.transactionHistoryReducer, 
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(TransactionHistory);
