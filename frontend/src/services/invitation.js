import { store } from '../store/data';

export const sendInvitations = async (emails) => {
  try {
    const newInvitations = emails.map(email => ({
      email,
      token: `inv_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
      invitedAt: new Date().toISOString()
    }));

    store.invitations.push(...newInvitations);
    return {
      flag: true,
      code: 200,
      message: "Invitations sent successfully",
      data: newInvitations
    };
  } catch (error) {
    return {
      flag: false,
      code: 500,
      message: "Internal server error",
      data: null
    };
  }
};