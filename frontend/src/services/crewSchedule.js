// import axios from 'axios';

// const API_URL = import.meta.env.VITE_API_URL;

// export const addCrewSchedule = async (gameId, crewAssignments) => {
//   try {
//     const response = await axios.post(`${API_URL}/crewSchedule/${gameId}`, 
//       crewAssignments.map(assignment => ({
//         crewedUserId: Date.now() + Math.floor(Math.random() * 1000),
//         ...assignment
//       }))
//     );
//     return {
//       flag: true,
//       code: 200,
//       message: "Crew scheduled successfully",
//       data: response.data
//     };
//   } catch (error) {
//     return {
//       flag: false,
//       code: error.response?.status || 500,
//       message: error.response?.data?.message || "Internal server error",
//       data: null
//     };
//   }
// };