import React, { useState } from "react";
import { useStore } from "../store/useStore";
import { toast } from "react-hot-toast";

function AdminDashboard() {
  const { crewMembers, games } = useStore();
  const [isInviting, setIsInviting] = useState(false);
  const [emails, setEmails] = useState([""]);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleAddEmail = () => {
    setEmails([...emails, ""]);
  };

  const handleRemoveEmail = (index) => {
    const newEmails = emails.filter((_, i) => i !== index);
    setEmails(newEmails.length ? newEmails : [""]);
  };

  const handleEmailChange = (index, value) => {
    const newEmails = [...emails];
    newEmails[index] = value;
    setEmails(newEmails);
  };

  const validateEmails = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const uniqueEmails = new Set(emails.filter((email) => email.trim()));

    if (uniqueEmails.size !== emails.filter((email) => email.trim()).length) {
      toast.error("Duplicate email addresses found");
      return false;
    }

    for (const email of emails) {
      if (email.trim() && !emailRegex.test(email)) {
        toast.error(`Invalid email address: ${email}`);
        return false;
      }
    }

    return true;
  };

  const handleInviteSubmit = async (e) => {
    e.preventDefault();

    if (!validateEmails()) {
      return;
    }

    const validEmails = emails.filter((email) => email.trim());
    if (!validEmails.length) {
      toast.error("Please enter at least one email address");
      return;
    }

    setIsSubmitting(true);
    try {
      // In a real app, this would be an API call
      await new Promise((resolve) => setTimeout(resolve, 1000));
      toast.success("Invitations sent successfully");
      setIsInviting(false);
      setEmails([""]);
    } catch (error) {
      toast.error("Failed to send invitations");
      console.error(error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="space-y-6">
      <header>
        <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
      </header>

      <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <h3 className="text-lg font-medium text-gray-900">Quick Stats</h3>
            <dl className="mt-4 grid grid-cols-1 gap-4">
              <div>
                <dt className="text-sm font-medium text-gray-500">
                  Total Crew Members
                </dt>
                <dd className="mt-1 text-3xl font-semibold text-gray-900">
                  {crewMembers.length}
                </dd>
              </div>
              <div>
                <dt className="text-sm font-medium text-gray-500">
                  Total Games
                </dt>
                <dd className="mt-1 text-3xl font-semibold text-gray-900">
                  {games.length}
                </dd>
              </div>
            </dl>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <h3 className="text-lg font-medium text-gray-900">Quick Actions</h3>
            <div className="mt-4 space-y-3">
              <button
                type="button"
                onClick={() => setIsInviting(true)}
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Invite New Crew Member
              </button>
              <button
                type="button"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Create New Game
              </button>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-5">
            <h3 className="text-lg font-medium text-gray-900">System Status</h3>
            <div className="mt-4">
              <div className="flex items-center">
                <div className="flex-shrink-0">
                  <span className="h-3 w-3 bg-green-400 rounded-full block"></span>
                </div>
                <p className="ml-2 text-sm text-gray-500">
                  All systems operational
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white shadow rounded-lg">
        <div className="px-4 py-5 sm:p-6">
          <h3 className="text-lg leading-6 font-medium text-gray-900">
            Recent Activity
          </h3>
          <div className="mt-5">
            <div className="flow-root">
              <ul className="-my-5 divide-y divide-gray-200">
                <li className="py-4">
                  <p className="text-sm text-gray-500">No recent activity</p>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      {isInviting && (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center">
          <div className="bg-white rounded-lg shadow-xl max-w-lg w-full mx-4">
            <div className="px-4 py-5 sm:p-6">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-lg font-medium text-gray-900">
                  Invite Crew Members
                </h3>
                <button
                  onClick={() => setIsInviting(false)}
                  className="text-gray-400 hover:text-gray-500"
                ></button>
              </div>
              <form onSubmit={handleInviteSubmit}>
                <div className="space-y-4">
                  {emails.map((email, index) => (
                    <div key={index} className="flex items-center space-x-2">
                      <input
                        type="email"
                        value={email}
                        onChange={(e) =>
                          handleEmailChange(index, e.target.value)
                        }
                        placeholder="Enter email address"
                        className="block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                      />
                      {emails.length > 1 && (
                        <button
                          type="button"
                          onClick={() => handleRemoveEmail(index)}
                          className="text-red-500 hover:text-red-700"
                        ></button>
                      )}
                    </div>
                  ))}
                </div>
                <div className="mt-4 flex items-center justify-between">
                  <button
                    type="button"
                    onClick={handleAddEmail}
                    className="inline-flex items-center px-3 py-1 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  >
                    Add Another Email
                  </button>
                  <div className="flex space-x-3">
                    <button
                      type="button"
                      onClick={() => setIsInviting(false)}
                      className="inline-flex justify-center py-2 px-4 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      Cancel
                    </button>
                    <button
                      type="submit"
                      disabled={isSubmitting}
                      className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50"
                    >
                      {isSubmitting ? "Sending..." : "Send Invitations"}
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default AdminDashboard;
