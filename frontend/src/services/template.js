// import axios from 'axios';

// const API_URL = import.meta.env.VITE_API_URL;

// export const getTemplate = async (userId) => {
//   try {
//     const response = await axios.get(`${API_URL}/template/${userId}`);
//     return {
//       flag: true,
//       code: 200,
//       message: 'Template found',
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