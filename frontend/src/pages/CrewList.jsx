import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useStore } from "../store/useStore";

function CrewList() {
  const navigate = useNavigate();
  const { crewMembers, currentUser, removeCrewMember } = useStore();
  const [searchTerm, setSearchTerm] = useState("");
  const [roleFilter, setRoleFilter] = useState("all");
  const [statusFilter, setStatusFilter] = useState("active");
  const [experienceFilter, setExperienceFilter] = useState("all");
  const [sortBy, setSortBy] = useState("name");
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const [selectedMembers, setSelectedMembers] = useState(new Set());

  const roles = Array.from(new Set(crewMembers.map((member) => member.role)));

  const handleSelectAll = (e) => {
    if (e.target.checked) {
      setSelectedMembers(new Set(filteredMembers.map((m) => m.userId)));
    } else {
      setSelectedMembers(new Set());
    }
  };

  const handleSelectMember = (userId) => {
    const newSelected = new Set(selectedMembers);
    if (newSelected.has(userId)) {
      newSelected.delete(userId);
    } else {
      newSelected.add(userId);
    }
    setSelectedMembers(newSelected);
  };

  const handleExportCSV = () => {
    const headers = ["Name", "Email", "Phone", "Role", "Positions"];
    const csvData = filteredMembers.map((member) => [
      `${member.firstName} ${member.lastName}`,
      member.email,
      member.phoneNumber || "",
      member.role,
      member.positions.join(", "),
    ]);

    const csv = [
      headers.join(","),
      ...csvData.map((row) => row.join(",")),
    ].join("\n");

    const blob = new Blob([csv], { type: "text/csv" });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "crew-members.csv";
    a.click();
    window.URL.revokeObjectURL(url);
  };

  const sortMembers = (members) => {
    return [...members].sort((a, b) => {
      switch (sortBy) {
        case "name":
          return `${a.firstName} ${a.lastName}`.localeCompare(
            `${b.firstName} ${b.lastName}`
          );
        case "role":
          return a.role.localeCompare(b.role);
        default:
          return 0;
      }
    });
  };

  const filteredMembers = sortMembers(
    crewMembers.filter((member) => {
      const fullName = `${member.firstName} ${member.lastName}`;
      const matchesSearch =
        fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        member.email.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesRole = roleFilter === "all" || member.role === roleFilter;
      const matchesExperience =
        experienceFilter === "all" || member.experience === experienceFilter;
      return matchesSearch && matchesRole && matchesExperience;
    })
  );

  const handleDelete = async (userId) => {
    setIsDeleting(true);
    const success = await removeCrewMember(userId);
    if (success) {
      setShowDeleteConfirm(null);
    }
    setIsDeleting(false);
  };

  return (
    <div className="space-y-6">
      <header className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-900">Crew Members</h1>
        {currentUser?.role === "ADMIN" && (
          <div className="flex items-center space-x-4">
            <button
              onClick={() => navigate("/admin/invite")}
              className="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
            >
              Invite Members
            </button>
            <button
              onClick={handleExportCSV}
              className="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              Export CSV
            </button>
          </div>
        )}
      </header>

      <div className="bg-white shadow rounded-lg">
        <div className="p-6">
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 mb-6">
            <div>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"></div>
                <input
                  type="text"
                  className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                  placeholder="Search by name or email..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
            </div>
            <div>
              <select
                className="block w-full pl-3 pr-10 py-2 border border-gray-300 rounded-md leading-5 bg-white focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                value={roleFilter}
                onChange={(e) => setRoleFilter(e.target.value)}
              >
                <option value="all">All Roles</option>
                {roles.map((role) => (
                  <option key={role} value={role}>
                    {role}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <select
                className="block w-full pl-3 pr-10 py-2 border border-gray-300 rounded-md leading-5 bg-white focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                value={experienceFilter}
                onChange={(e) => setExperienceFilter(e.target.value)}
              >
                <option value="all">All Experience Levels</option>
                <option value="new">New</option>
                <option value="experienced">Experienced</option>
                <option value="senior">Senior</option>
              </select>
            </div>
            <div>
              <select
                className="block w-full pl-3 pr-10 py-2 border border-gray-300 rounded-md leading-5 bg-white focus:outline-none focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
              >
                <option value="name">Sort by Name</option>
                <option value="role">Sort by Role</option>
              </select>
            </div>
          </div>

          {currentUser?.role === "ADMIN" && (
            <div className="mb-4 flex items-center">
              <input
                type="checkbox"
                className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                checked={selectedMembers.size === filteredMembers.length}
                onChange={handleSelectAll}
              />
              <span className="ml-2 text-sm text-gray-500">
                {selectedMembers.size} selected
              </span>
            </div>
          )}

          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
            {filteredMembers.map((member) => (
              <div
                key={member.userId}
                className="bg-white border rounded-lg overflow-hidden hover:shadow-lg transition-shadow duration-200"
              >
                <div className="p-6">
                  <div className="flex items-center">
                    {currentUser?.role === "ADMIN" && (
                      <input
                        type="checkbox"
                        className="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded mr-4"
                        checked={selectedMembers.has(member.userId)}
                        onChange={() => handleSelectMember(member.userId)}
                      />
                    )}
                    <div
                      className="h-12 w-12 rounded-full bg-gray-200 flex items-center justify-center cursor-pointer"
                      onClick={() => navigate(`/profile/${member.userId}`)}
                    >
                      <span className="text-xl font-medium text-gray-600">
                        {member.firstName.charAt(0)}
                      </span>
                    </div>
                    <div className="ml-4 flex-1">
                      <h3 className="text-lg font-medium text-gray-900">
                        {member.firstName} {member.lastName}
                      </h3>
                      <p className="text-sm text-gray-500">{member.role}</p>
                    </div>
                    {currentUser?.role === "ADMIN" &&
                      member.userId !== currentUser.userId && (
                        <button
                          onClick={() => setShowDeleteConfirm(member.userId)}
                          className="text-red-500 hover:text-red-700"
                        >
                          Delete
                        </button>
                      )}
                  </div>
                  <div className="mt-4 space-y-2">
                    <div className="flex items-center text-sm text-gray-500">
                      {member.email}
                    </div>
                    {member.phoneNumber && (
                      <div className="flex items-center text-sm text-gray-500">
                        {member.phoneNumber}
                      </div>
                    )}
                  </div>
                  <div className="mt-2">
                    <div className="flex flex-wrap gap-1">
                      {member.positions.map((position) => (
                        <span
                          key={position}
                          className="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-indigo-100 text-indigo-800"
                        >
                          {position.replace("_", " ")}
                        </span>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {filteredMembers.length === 0 && (
            <div className="text-center py-12">
              <h3 className="mt-2 text-lg font-medium text-gray-900">
                No crew members found
              </h3>
              <p className="mt-1 text-sm text-gray-500">
                Try adjusting your search or filters to find what you're looking
                for.
              </p>
            </div>
          )}
        </div>
      </div>

      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center">
          <div className="bg-white rounded-lg p-6 max-w-sm mx-4">
            <div className="flex items-center justify-center mb-4"></div>
            <h3 className="text-lg font-medium text-gray-900 text-center mb-2">
              Confirm Deletion
            </h3>
            <p className="text-sm text-gray-500 text-center mb-6">
              Are you sure you want to delete this crew member? This action
              cannot be undone.
            </p>
            <div className="flex justify-center space-x-4">
              <button
                onClick={() => setShowDeleteConfirm(null)}
                className="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                disabled={isDeleting}
              >
                Cancel
              </button>
              <button
                onClick={() => handleDelete(showDeleteConfirm)}
                className="px-4 py-2 border border-transparent rounded-md text-sm font-medium text-white bg-red-600 hover:bg-red-700 disabled:opacity-50"
                disabled={isDeleting}
              >
                {isDeleting ? "Deleting..." : "Delete"}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CrewList;
