import app from '@src/app.js';
import { jest } from '@jest/globals';

const helper = {
  pause: async (seconds = 1) =>
    new Promise(resolve => setTimeout(resolve, seconds * 1000)),
};

global.app = app;
global.helper = helper;
global.jest = jest;
