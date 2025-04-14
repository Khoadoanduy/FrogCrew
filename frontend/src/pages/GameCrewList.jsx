import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Calendar, Clock, MapPin, Users, AlertCircle, Plus, X, Check } from 'lucide-react';
import { useStore } from '../store/useStore';
import { format } from 'date-fns';
import { toast } from 'react-hot-toast';

function GameCrewList() {
  const { gameId } = useParams();
  const navigate = useNavigate();
  const { games, crewMembers, currentUser, assignCrewMember, removeCrewMember } = useStore();
  const [selectedPosition, setSelectedPosition] = useState(null);
  const [showAssignModal, setShowAssignModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  
  const game = games.find(g => g.gameId === parseInt(gameId));

  if (!game) {
    return (
      <div className="text-center py-12">
        <AlertCircle className="mx-auto h-12 w-12 text-gray-400" />
        <h3 className="mt-2 text-lg font-medium text-gray-900">Game not found</h3>
        <p className="mt-1 text-sm text-gray-500">The game you're looking for doesn't exist.</p>
      </div>
    );
  }

  const positions = [
    'PRODUCER',
    'ASST_PROD',
    'DIRECTOR',
    'ASST_DIRECTOR',
    'TECHNICAL_DIR',
    'GRAPHICS',
    'BUG_OP',
    'REPLAY_EVS',
    'EIC',
    'VIDEO',
    'AUDIO',
    'CAMERA',
    'UTILITY',
    'TECH_MANAGER',
    'TOC',
    'OBSERVER'
  ];

  const getAvailableCrewMembers = (position) => {
    return crewMembers.filter(member => {
      // Check if member is qualified for the position
      const isQualified = member.positions.includes(position);
      
      // Check if member is already assigned to this game
      const isAssigned = game.crewedMembers.some(cm => cm.userId === member.userId);
      
      // Check if member is available for this game
      const isAvailable = true; // This would check against the availability submissions

      return isQualified && !isAssigned && isAvailable;
    });
  };

  const handleAssign = async (position, crewMember) => {
    try {
      await assignCrewMember({
        gameId: game.gameId,
        userId: crewMember.userId,
        position: position,
        reportTime: game.gameStart, // Default to game start time
        reportLocation: "CONTROL ROOM" // Default location
      });
      
      setShowAssignModal(false);
      setSelectedPosition(null);
      toast.success(`${crewMember.firstName} ${crewMember.lastName} assigned as ${position}`);
    } catch (error) {
      toast.error('Failed to assign crew member');
      console.error(error);
    }
  };

  const handleRemove = async (crewMemberId) => {
    try {
      await removeCrewMember(game.gameId, crewMemberId);
      toast.success('Crew member removed from assignment');
    } catch (error) {
      toast.error('Failed to remove crew member');
      console.error(error);
    }
  };

  const filteredCrewMembers = (position) => {
    const available = getAvailableCrewMembers(position);
    return available.filter(member => 
      `${member.firstName} ${member.lastName}`.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.email.toLowerCase().includes(searchTerm.toLowerCase())
    );
  };

  return (
    <div className="space-y-6">
      <header>
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-900">
            Crew List - {game.opponent}
          </h1>
          <button
            onClick={() => navigate('/schedule')}
            className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
          >
            Back to Schedule
          </button>
        </div>
        <div className="mt-4 grid grid-cols-1 gap-4 sm:grid-cols-3">
          <div className="flex items-center text-sm text-gray-500">
            <Calendar className="mr-2 h-5 w-5" />
            {format(new Date(game.gameDate), 'MMMM dd, yyyy')}
          </div>
          <div className="flex items-center text-sm text-gray-500">
            <Clock className="mr-2 h-5 w-5" />
            Game Start: {game.gameStart}
          </div>
          <div className="flex items-center text-sm text-gray-500">
            <MapPin className="mr-2 h-5 w-5" />
            {game.venue}
          </div>
        </div>
      </header>

      <div className="bg-white shadow overflow-hidden sm:rounded-lg">
        <div className="px-4 py-5 sm:px-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">
            Assigned Crew Members
          </h3>
          <p className="mt-1 max-w-2xl text-sm text-gray-500">
            {game.crewedMembers.length} crew members assigned
          </p>
        </div>
        <div className="border-t border-gray-200">
          <div className="bg-gray-50 px-4 py-5 grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {positions.map((position) => {
              const crewMember = game.crewedMembers.find(
                member => member.Position === position
              );

              return (
                <div
                  key={position}
                  className={`p-4 rounded-lg ${
                    crewMember ? 'bg-white shadow' : 'bg-gray-100 border-2 border-dashed border-gray-300'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <h4 className="text-sm font-medium text-gray-900">
                      {position.replace('_', ' ')}
                    </h4>
                    {currentUser?.role === 'ADMIN' && (
                      crewMember ? (
                        <button
                          onClick={() => handleRemove(crewMember.userId)}
                          className="text-red-600 hover:text-red-800"
                        >
                          <X className="h-4 w-4" />
                        </button>
                      ) : (
                        <button
                          onClick={() => {
                            setSelectedPosition(position);
                            setShowAssignModal(true);
                          }}
                          className="text-indigo-600 hover:text-indigo-800"
                        >
                          <Plus className="h-4 w-4" />
                        </button>
                      )
                    )}
                  </div>
                  {crewMember ? (
                    <div className="mt-2">
                      <div className="flex items-center">
                        <div className="flex-shrink-0">
                          <div className="h-8 w-8 rounded-full bg-indigo-100 flex items-center justify-center">
                            <span className="text-sm font-medium text-indigo-800">
                              {crewMember.fullName.charAt(0)}
                            </span>
                          </div>
                        </div>
                        <div className="ml-3">
                          <p className="text-sm font-medium text-gray-900">
                            {crewMember.fullName}
                          </p>
                          <div className="flex items-center mt-1 text-xs text-gray-500">
                            <Clock className="mr-1 h-3 w-3" />
                            Report: {crewMember.ReportTime}
                          </div>
                          <div className="flex items-center mt-1 text-xs text-gray-500">
                            <MapPin className="mr-1 h-3 w-3" />
                            {crewMember.ReportLocation}
                          </div>
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div className="mt-2 flex items-center text-sm text-gray-500">
                      <Users className="mr-1 h-4 w-4" />
                      Unassigned
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {showAssignModal && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center">
          <div className="bg-white rounded-lg p-6 max-w-lg w-full mx-4">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-medium text-gray-900">
                Assign {selectedPosition.replace('_', ' ')}
              </h3>
              <button
                onClick={() => {
                  setShowAssignModal(false);
                  setSelectedPosition(null);
                }}
                className="text-gray-400 hover:text-gray-500"
              >
                <X className="h-5 w-5" />
              </button>
            </div>

            <div className="mb-4">
              <input
                type="text"
                placeholder="Search crew members..."
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </div>

            <div className="max-h-96 overflow-y-auto">
              <ul className="divide-y divide-gray-200">
                {filteredCrewMembers(selectedPosition).map((member) => (
                  <li key={member.userId} className="py-4">
                    <div className="flex items-center justify-between">
                      <div>
                        <h4 className="text-sm font-medium text-gray-900">
                          {member.firstName} {member.lastName}
                        </h4>
                        <p className="text-sm text-gray-500">{member.email}</p>
                      </div>
                      <button
                        onClick={() => handleAssign(selectedPosition, member)}
                        className="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded-full shadow-sm text-white bg-indigo-600 hover:bg-indigo-700"
                      >
                        <Check className="h-4 w-4 mr-1" />
                        Assign
                      </button>
                    </div>
                  </li>
                ))}
                {filteredCrewMembers(selectedPosition).length === 0 && (
                  <li className="py-4 text-center text-sm text-gray-500">
                    No available crew members found for this position
                  </li>
                )}
              </ul>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default GameCrewList;