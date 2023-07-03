import axios from "axios";
import debug from "axios-debug-log";
import { backend_url } from "../../utils/conf";

const api = axios.create({
  baseURL: backend_url,
});

debug.addLogger(axios);

export default api;
