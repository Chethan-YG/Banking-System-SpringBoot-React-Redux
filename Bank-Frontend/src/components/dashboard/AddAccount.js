import React, { useState } from "react";
import Button from "@mui/material/Button";
import Drawer from "@mui/material/Drawer";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import alertify from "alertifyjs";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import * as accountActions from "../../redux/actions/accountActions";
import apiClient from "../../apiClient";  // Import the apiClient you created

function AccountForm({ onSaveAccount, open = true, onClose, actions }) {
  const [accountInfo, setAccountInfo] = useState({
    accountName: "",
    accountType: "",
  });

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setAccountInfo({
      ...accountInfo,
      [name]: value,
    });
  };

  const handleSaveAccount = async (event) => {
    event.preventDefault();

    const accessToken = localStorage.getItem("access_token");

    if (!accessToken) {
      alertify.error("You need to be logged in to create an account.");
      window.location.href = "/"; // Optionally, redirect the user to login page
      return;
    }

    const jsonData = {
      accountName: accountInfo.accountName,
      accountType: accountInfo.accountType,
    };

    try {
      const apiUrl = "/account/create_account"; // Use relative URL
      const response = await apiClient.post(apiUrl, jsonData, {
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.status === 201) {
        alertify.success("New account added successfully.");
        actions.getAccounts(); // Dispatch action to refresh account list
        onSaveAccount(accountInfo); // Callback to parent
        onClose(); // Close the form
      }
    } catch (error) {
      console.error("Error creating account:", error);
      alertify.error("Something went wrong. Please try again.");
    }
  };

  return (
    <Drawer
      anchor="right"
      open={open}
      onClose={onClose}
      ModalProps={{
        disableScrollLock: true,
      }}
    >
      <div
        style={{
          width: "300px",
          padding: "16px",
        }}
        role="presentation"
        onClick={onClose}
        onKeyDown={onClose}
      >
        <Typography variant="h6" style={{ marginTop: "60px" }}>
          Add Account
        </Typography>
        <TextField
          style={{ marginTop: "60px" }}
          name="accountName"
          label="Account Name"
          value={accountInfo.accountName}
          onChange={handleInputChange}
          fullWidth
          margin="normal"
          onClick={(event) => event.stopPropagation()}
          onKeyDown={(event) => event.stopPropagation()}
        />
        <TextField
          name="accountType"
          label="Account Type"
          value={accountInfo.accountType}
          onChange={handleInputChange}
          fullWidth
          margin="normal"
          onClick={(event) => event.stopPropagation()}
          onKeyDown={(event) => event.stopPropagation()}
        />
        <Box mt={2}>
          <Button
            variant="contained"
            color="primary"
            onClick={handleSaveAccount}
            sx={{ backgroundColor: "#EB3D13" }}
          >
            Create Account
          </Button>
        </Box>
      </div>
    </Drawer>
  );
}

function mapDispatchToProps(dispatch) {
  return {
    actions: {
      getAccounts: bindActionCreators(accountActions.getAccounts, dispatch),
    },
  };
}

export default connect(null, mapDispatchToProps)(AccountForm);
