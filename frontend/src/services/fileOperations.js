import fs from "fs";
import path from "path";

export const updateDbFile = async (data) => {
  try {
    const filePath = path.resolve(__dirname, "../../db.json");
    await fs.promises.writeFile(filePath, JSON.stringify(data, null, 2));
    return true;
  } catch (error) {
    console.error("Error updating db.json:", error);
    return false;
  }
};
