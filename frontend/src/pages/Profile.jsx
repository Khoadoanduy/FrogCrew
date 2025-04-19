import React from "react";
import { useParams } from "react-router-dom";
import { useStore } from "../store/useStore";

function Profile() {
  const { id } = useParams();
  const { crewMembers, currentUser } = useStore();
  const crewMember = crewMembers.find((member) => member.userId === id);

  if (!crewMember) {
    return (
      <div className="text-center py-12">
        <h2 className="text-2xl font-semibold text-gray-900">
          Crew member not found
        </h2>
      </div>
    );
  }

  const isAdmin = currentUser?.role === "ADMIN";
  const fullName = `${crewMember.firstName} ${crewMember.lastName}`;

  return (
    <div className="max-w-3xl mx-auto">
      <div className="bg-white shadow rounded-lg overflow-hidden">
        <div className="relative h-32 bg-gradient-to-r from-indigo-500 to-purple-600">
          <div className="absolute -bottom-16 left-8">
            <div className="h-32 w-32 rounded-full border-4 border-white shadow-lg bg-gray-100 flex items-center justify-center">
              <span className="text-4xl font-medium text-gray-600">
                {crewMember.firstName.charAt(0)}
              </span>
            </div>
          </div>
        </div>

        <div className="pt-20 pb-8 px-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">{fullName}</h1>
              <div className="flex items-center mt-2">
                <span className="ml-2 text-lg text-gray-600">
                  {crewMember.role}
                </span>
              </div>
            </div>
            {crewMember.role === "ADMIN" && (
              <div className="flex items-center">
                <span className="ml-2 font-medium">Admin</span>
              </div>
            )}
          </div>

          <div className="mt-8 grid grid-cols-1 gap-6 sm:grid-cols-2">
            <div className="flex items-center p-4 bg-gray-50 rounded-lg">
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-500">Email</p>
                <p className="mt-1 text-sm text-gray-900">{crewMember.email}</p>
              </div>
            </div>

            {crewMember.phoneNumber && (
              <div className="flex items-center p-4 bg-gray-50 rounded-lg">
                <div className="ml-4">
                  <p className="text-sm font-medium text-gray-500">Phone</p>
                  <p className="mt-1 text-sm text-gray-900">
                    {crewMember.phoneNumber}
                  </p>
                </div>
              </div>
            )}
          </div>

          <div className="mt-8">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">
              Positions
            </h2>
            <div className="flex flex-wrap gap-2">
              {crewMember.positions.map((position) => (
                <div
                  key={position}
                  className="flex items-center px-3 py-1 rounded-full bg-indigo-100 text-indigo-800"
                >
                  <span className="text-sm font-medium">
                    {position.replace("_", " ")}
                  </span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
