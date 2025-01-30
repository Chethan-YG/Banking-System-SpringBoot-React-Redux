import React, { useEffect, useState } from "react";
import { connect } from "react-redux";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import * as accountActions from "../../redux/actions/accountActions";

function Chart(props) {
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    const processData = () => {
      const data = props.transactHistory
        .filter(
          (item) =>
            item.status !== "failed" && item.transactionType !== "Transfer"
        )
        .sort((a, b) => a.transactionId - b.transactionId) 
        .map((item) => ({
          time: dateFormatter(item.createdAt),
          amount: calculateTotalAmount(item),
        }))
        .slice(-9);

      setChartData(data);
    };

    processData();
  }, [props.transactHistory]);

  function dateFormatter(createdAt) {
    console.log(createdAt)
    if (typeof createdAt === "string") {
      const [datePart] = createdAt.split("T"); 
      const [year, month, day] = datePart.split("-"); 
      return `${month}/${day}`;
    }
    return "Invalid Date";
  }

  function calculateTotalAmount(item) {
    return item.transactionType === "deposit" ? item.amount : -item.amount;
  }

  return (
    <div>
      <h2>Transaction History Chart</h2>
      <ResponsiveContainer width="100%" height={400}>
        <LineChart data={chartData}>
          <XAxis dataKey="time" />
          <YAxis />
          <CartesianGrid stroke="#ccc" />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="amount" stroke="#8884d8" />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
}

function mapStateToProps(state) {
  return {
    transactHistory: state.transactionHistoryReducer,
  };
}

export default connect(mapStateToProps)(Chart);






