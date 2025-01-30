import * as React from "react";
import { useState, useEffect } from "react";
import { styled, createTheme, ThemeProvider } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";
import MuiDrawer from "@mui/material/Drawer";
import Box from "@mui/material/Box";
import MuiAppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import List from "@mui/material/List";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import Link from "@mui/material/Link";
import MenuIcon from "@mui/icons-material/Menu";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import { mainListItems, secondaryListItems } from "./listItems";
import Chart from "./Chart";
import TotalAccountBalance from "./TotalAccountBalance";
import Accounts from "./Accounts";
import axios from "axios";
import Button from "@mui/material/Button";
import alertify from "alertifyjs";
import { useNavigate } from "react-router-dom";
import AddAccount from "./AddAccount";
import TransactionHistory from "./TransactionHistory";

// For Footer
function Copyright(props) {
  return (
    <Typography
      variant="body2"
      color="text.secondary"
      align="center"
      {...props}
    >
      {" © "}
      <Link
        color="inherit"
        style={{ textDecoration: "none" }}
        href="https://mui.com/"
      >
        Created By Chethan Y G
      </Link>{" "}
      {new Date().getFullYear()}
      {"."}
    </Typography>
  );
}

// Drawer width
const drawerWidth = 240;

// Styled AppBar
const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(["width", "margin"], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

// Styled Drawer
const Drawer = styled(MuiDrawer, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  "& .MuiDrawer-paper": {
    position: "relative",
    whiteSpace: "nowrap",
    width: drawerWidth,
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
    boxSizing: "border-box",
    ...(!open && {
      overflowX: "hidden",
      transition: theme.transitions.create("width", {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      width: theme.spacing(7),
      [theme.breakpoints.up("sm")]: {
        width: theme.spacing(9),
      },
    }),
  },
}));

const defaultTheme = createTheme();

// Dashboard Component
export default function Dashboard() {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("access_token");

    if (!token) {
      alertify.error("User not logged in");
      navigate("/"); // Redirect to login
    }
  }, [navigate]);

  // Handle drawer toggle
  const toggleDrawer = () => setOpen(!open);

  const handleSignOut = (event) => {
    event.preventDefault();
  
    // Clear the tokens from localStorage
    localStorage.removeItem("access_token");
    localStorage.removeItem("refresh_token");
  
    // Show logout success message
    alertify.success("Logout successful.");
  
    // Redirect to the login page
    navigate("/"); // Redirect to login page (assuming / is your login route)
  };
  

  const [isFormOpen, setIsFormOpen] = useState(false);

  const handleSaveAccount = (accountInfo) => {
    console.log("New account information:", accountInfo);
  };

  return (
    <ThemeProvider theme={defaultTheme}>
      <Box sx={{ display: "flex" }}>
        <CssBaseline />
        <AppBar position="absolute" open={open} sx={{ backgroundColor: "#4CAF50" }}>
          <Toolbar sx={{ pr: "24px" }}>
            <IconButton
              edge="start"
              color="inherit"
              aria-label="open drawer"
              onClick={toggleDrawer}
              sx={{ marginRight: "36px", ...(open && { display: "none" }) }}
            >
              <MenuIcon />
            </IconButton>
            <Typography component="h1" variant="h6" color="inherit" noWrap sx={{ flexGrow: 1 }}>
              Chethan Bank
            </Typography>
            <Button color="inherit" onClick={handleSignOut}>
              Logout
            </Button>
          </Toolbar>
        </AppBar>
        <Drawer variant="permanent" open={open}>
          <Toolbar sx={{ display: "flex", alignItems: "center", justifyContent: "flex-end", px: [1] }}>
            <IconButton onClick={toggleDrawer}>
              <ChevronLeftIcon />
            </IconButton>
          </Toolbar>
          <Divider />
          <List component="nav">{mainListItems}</List>
          <Divider sx={{ my: 1 }} />
          <List component="nav">{secondaryListItems}</List>
        </Drawer>
        <Box
          component="main"
          sx={{
            backgroundColor: (theme) => theme.palette.mode === "light" ? theme.palette.grey[100] : theme.palette.grey[900],
            flexGrow: 1,
            height: "100vh",
            overflow: "auto",
          }}
        >
          <Toolbar />
          <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
            <Grid container spacing={3}>
              <Grid item xs={12} md={8} lg={9}>
                <Paper sx={{ p: 2, display: "flex", flexDirection: "column", height: 500 }}>
                  <Chart />
                </Paper>
              </Grid>
              <Grid item xs={12} md={4} lg={3}>
                <Paper sx={{ p: 2, display: "flex", flexDirection: "column", height: 240 }}>
                  <TotalAccountBalance />
                </Paper>
              </Grid>
              <Grid item xs={12}>
                <Paper sx={{ p: 2, display: "flex", flexDirection: "column" }}>
                  <Accounts />
                </Paper>
              </Grid>
              <Grid item xs={12} container justifyContent="flex-end">
                <Button
                  variant="contained"
                  color="primary"
                  onClick={() => setIsFormOpen(true)}
                  sx={{ backgroundColor: "#EB3D13" }}
                >
                  Add Account
                </Button>
                <AddAccount onSaveAccount={handleSaveAccount} open={isFormOpen} onClose={() => setIsFormOpen(false)} />
              </Grid>
            </Grid>
            <Grid>
              <Paper sx={{ p: 2, display: "flex", flexDirection: "column", marginTop: 2 }}>
                <TransactionHistory />
              </Paper>
            </Grid>
            <Copyright sx={{ pt: 4 }} />
          </Container>
        </Box>
      </Box>
    </ThemeProvider>
  );
}
