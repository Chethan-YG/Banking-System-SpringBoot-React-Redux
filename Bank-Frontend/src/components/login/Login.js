import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Link from "@mui/material/Link";
import Paper from "@mui/material/Paper";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { NavLink, useNavigate } from "react-router-dom";
import axios from "axios";
import alertify from "alertifyjs";

const theme = createTheme({
  palette: {
    primary: {
      main: "#4CAF50",
    },
    secondary: {
      main: "#FF5722",
    },
    blue: {
      main: "#2B8EF9",
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

export default function SignInSide() {
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const jsonData = {
      email: data.get("email").toString(),
      password: data.get("password").toString(),
    };

    try {
      const apiUrl = "http://localhost:8070/api/auth/signin";
      const response = await axios.post(apiUrl, jsonData, {
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.status === 200 && response.data.token) {
        localStorage.setItem("access_token", response.data.token);
        localStorage.setItem("refresh_token", response.data.refreshToken);

        alertify.success("Login successful.");
        navigate("/dashboard");
      } else {
        alertify.error("Login failed. Invalid credentials.");
      }
    } catch (error) {
      if (error.response) {
        alertify.error(
          "Invalid Credentials" );
        console.error("Error response:", error.response);
      } else if (error.request) {
        alertify.error("Login failed. No response from the server.");
        console.error("Error request:", error.request);
      } else {
        alertify.error("Login failed. An unknown error occurred.");
        console.error("Error message:", error.message);
      }
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <Grid container component="main" sx={{ height: "100vh" }}>
        <CssBaseline />
        <Grid
          item
          xs={false}
          sm={4}
          md={7}
          sx={{
            backgroundImage: "url(/bank.jpg)",
            backgroundRepeat: "no-repeat",
            backgroundColor: (t) =>
              t.palette.mode === "light"
                ? t.palette.grey[50]
                : t.palette.grey[900],
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        />
        <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
          <Box
            sx={{
              my: 4,
              mx: 4,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: theme.palette.primary.main }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Sign in
            </Typography>
            <Box
              component="form"
              noValidate
              onSubmit={handleSubmit}
              sx={{ mt: 1 }}
            >
              <TextField
                margin="normal"
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                autoFocus
              />
              <TextField
                margin="normal"
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
              />

              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{
                  mt: 3,
                  mb: 2,
                  bgcolor: theme.palette.secondary.main,
                  "&:hover": {
                    bgcolor: "#E64A19",
                  },
                  color: "white",
                }}
              >
                Sign In
              </Button>
              <Grid container>
                <Grid item xs>
                  <Link href="#" variant="body2">
                    Forgot password?
                  </Link>
                </Grid>
                <Grid item>
                  <Typography variant="body2">
                    <NavLink
                      to="/register"
                      style={{
                        textDecoration: "none",
                      }}
                    >
                      <span
                        style={{
                          color: "grey",
                        }}
                      >
                        Not Registered?
                      </span>{" "}
                      <span
                        style={{
                          color: theme.palette.blue.main, 
                        }}
                      >
                        Create an account
                      </span>
                    </NavLink>
                  </Typography>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Grid>
      </Grid>
    </ThemeProvider>
  );
}
