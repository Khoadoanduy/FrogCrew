import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import CrewList from './pages/CrewList';
import GameCrewList from './pages/GameCrewList';
import Schedule from './pages/Schedule';
import Profile from './pages/Profile';
import AdminDashboard from './pages/AdminDashboard';
import CreateGameSchedule from './pages/CreateGameSchedule';
import EditGameSchedule from './pages/EditGameSchedule';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import { useStore } from './store/useStore';

function App() {
  const { currentUser } = useStore();

  return (
    <BrowserRouter>
      <Toaster position="top-right" />
      <Routes>
        {/* Auth routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />

        {/* Protected routes */}
        <Route path="/" element={<Layout />}>
          <Route index element={<Dashboard />} />
          <Route path="crew" element={<CrewList />} />
          <Route path="crew/game/:gameId" element={<GameCrewList />} />
          <Route path="schedule" element={<Schedule />} />
          <Route path="schedule/create" element={<CreateGameSchedule />} />
          <Route path="schedule/edit/:gameId" element={<EditGameSchedule />} />
          <Route path="profile/:id" element={<Profile />} />
          {currentUser?.role === 'ADMIN' && (
            <Route path="admin" element={<AdminDashboard />} />
          )}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;