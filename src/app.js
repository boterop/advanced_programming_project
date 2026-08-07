import './config/globals.js';
import express from 'express';
import cors from 'cors';
import guardian from './middlewares/guardian.js';

const app = express();

app.use(express.json());
app.use(cors());
app.use(guardian);

app.get('/health', (_req, res) => {
  res.status(200).send('OK');
});

export default app;
