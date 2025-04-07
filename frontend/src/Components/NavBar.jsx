import React from "react";

const NavBar = () => {
  return (
    <nav className="bg-white shadow-lg">
      <div className="container mx-auto px-4">
        <div className="flex items-center h-16">
          <a href="#" className="text-xl font-bold text-indigo-600 mr-8">
            FrogCrew
          </a>

          <div className="flex space-x-4">
            <a
              href="#"
              className="flex items-center space-x-1 px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100"
            >
              <span>Home Page</span>
            </a>
            <a
              href="#"
              className="flex items-center space-x-1 px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100"
            >
              <span>Schedule</span>
            </a>

            <a
              href="#"
              className="flex items-center space-x-1 px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100"
            >
              <span>Crew Members</span>
            </a>
            <a
              href="#"
              className="flex items-center space-x-1 px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100"
            >
              <span>Trade Board</span>
            </a>

            <a
              href="#"
              className="flex items-center space-x-1 px-3 py-2 rounded-md text-sm font-medium text-gray-700 hover:bg-gray-100"
            >
              <span>Reports</span>
            </a>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
