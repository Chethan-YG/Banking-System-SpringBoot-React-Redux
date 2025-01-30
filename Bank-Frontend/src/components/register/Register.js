import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Checkbox from "@mui/material/Checkbox";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import axios from "axios";
import { NavLink, useNavigate } from "react-router-dom";
import alertify from "alertifyjs";

const customTheme = createTheme({
  palette: {
    primary: {
      main: "#4CAF50", // Green
    },
    secondary: {
      main: "#FF5722", // Orange
    },
    blue: {
      main: "#2B8EF9", //blue
    },
  },
  typography: {
    h5: {
      fontWeight: 600,
    },
    body1: {
      fontFamily: "Roboto, Arial, sans-serif",
    },
  },
  shape: {
    borderRadius: 8, 
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 25,
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          borderRadius: 12,
        },
      },
    },
  },
});

export default function SignUp() {
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const jsonData = {
      firstname: data.get("firstName"),
      lastname: data.get("lastName"),
      email: data.get("email"),
      password: data.get("password"),
    };

    const confirmPassword = data.get("confirmPassword");

    if (jsonData.password !== confirmPassword) {
      alertify.error("Passwords do not match.");
      return;
    }

    try {
      const apiUrl = `http://localhost:8070/register?confirm_password=${confirmPassword}`;
      const response = await axios.post(apiUrl, jsonData, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      console.log(response)
      if (response.status === 200) {
        alertify.success("Registration success. Please check your email and verify your account.");
        navigate("/");
      }
    } catch (error) {
      console.error("Error registering user:", error);

      if (error.response) {
        alertify.error(
          `Registration failed: ${error.response.data || "Unknown error"}`
        );
      } else if (error.request) {
        alertify.error("No response received from server.");
      } else {
        alertify.error(`Error: ${error.message}`);
      }
    }
  };

  return (
    <ThemeProvider theme={customTheme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 4,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: customTheme.palette.primary.main }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="given-name"
                  name="firstName"
                  required
                  fullWidth
                  id="firstName"
                  label="First Name"
                  autoFocus
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  autoComplete="family-name"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="confirmPassword"
                  label="Confirm Password"
                  type="password"
                  id="confirmPassword"
                  autoComplete="new-password"
                />
              </Grid>
              <Grid item xs={12}>
                <Checkbox value="agreeToTerms" color="primary" required />
                <span style={{ fontSize: "0.875rem" }}>
                  I agree to the{" "}
                  <a
                    href="/terms-and-conditions"
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{ color: "#4CAF50", textDecoration: "none" }}
                  >
                    Terms and Conditions
                  </a>{" "}
                  and{" "}
                  <a
                    href="/privacy-policy"
                    target="_blank"
                    rel="noopener noreferrer"
                    style={{ color: "#4CAF50", textDecoration: "none" }}
                  >
                    Privacy Policy
                  </a>
                </span>
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="secondary"
              sx={{
                mt: 3,
                mb: 2,
                borderRadius: customTheme.shape.borderRadius,
                "&:hover": {
                  bgcolor: "#E64A19",
                },
              }}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <Typography variant="body2">
                  <NavLink
                    to="/"
                    style={{
                      textDecoration: "none",
                    }}
                  >
                    <span
                      style={{
                        color: "grey", 
                      }}
                    >
                      Already have an account?{" "}
                    </span>
                    <span
                      style={{
                        color: customTheme.palette.blue.main,
                      }}
                    >
                      Sign in
                    </span>
                  </NavLink>
                </Typography>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
