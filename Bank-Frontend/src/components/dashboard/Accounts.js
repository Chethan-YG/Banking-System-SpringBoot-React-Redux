import * as React from "react";
import Link from "@mui/material/Link";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Title from "./Title";
import { useEffect, useState } from "react";
import { connect } from "react-redux";
import { bindActionCreators } from "redux";
import * as accountActions from "../../redux/actions/accountActions";
import Button from "@mui/material/Button";
import Transact from "./Transact";

function preventDefault(event) {
  event.preventDefault();
}

function Accounts(props) {
  const [isFormOpen, setIsFormOpen] = useState(false);

  const handleSaveAccount = (accountInfo) => {
    console.log("New account information:", accountInfo);
  };

  useEffect(() => {
    props.actions.getAccounts();
  }, [props.actions]);


  return (
    <React.Fragment>
      <Title>{props.currentAccount.accountName},  Your Accounts </Title>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Account Id</TableCell>
            <TableCell>Account Name</TableCell>
            <TableCell>Account Number</TableCell>
            <TableCell>Account Type</TableCell>
            <TableCell>User ID</TableCell>
            <TableCell>Updated At</TableCell>
            <TableCell>Account Balance</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {props.accounts.map((account) => (
            <TableRow
              onClick={() => {
                props.actions.changeAccount(account);
              }}
              key={account.accountId}
            >
              <TableCell>{account.accountId}</TableCell>
              <TableCell>{account.accountName}</TableCell>
              <TableCell>{account.accountNumber}</TableCell>
              <TableCell>{account.accountType}</TableCell>
              <TableCell>{account.user_id}</TableCell>
              <TableCell>{`$${account.balance}`}</TableCell>
              <TableCell align="right">
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => setIsFormOpen(true)}
                  sx={{ backgroundColor: "#F5BD52" }}
                >
                  Transact
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      <Transact
        onSaveAccount={handleSaveAccount}
        open={isFormOpen}
        onClose={() => setIsFormOpen(false)}
      />
      <Link color="primary" href="#" onClick={preventDefault} sx={{ mt: 3 }}>
        See more accounts
      </Link>
    </React.Fragment>
  );
}

function mapDispatchToProps(dispatch) {
  return {
    actions: {
      getAccounts: bindActionCreators(accountActions.getAccounts, dispatch),
      changeAccount: bindActionCreators(accountActions.changeAccount, dispatch),
    },
  };
}

function mapStateToProps(state) {
  return {
    currentAccount: state.changeAccountReducer,
    accounts: state.accountListReducer,
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(Accounts);
