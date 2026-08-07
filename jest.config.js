import './src/config/globals.js';

// Load environment variables
try {
  process.loadEnvFile(process.cwd() + '/.env.test');
} catch (_error) {
  logger.info('No se encontró el archivo de variables de entorno .env.test');
}

process.env.NODE_ENV = 'test';
process.env.JWT_SECRET = 'test-secret';
process.env.JWT_EXPIRES_IN = '1h';

export default {
  testEnvironment: 'node',
  moduleFileExtensions: ['js', 'json'],
  moduleNameMapper: {
    '^@src/(.*)$': '<rootDir>/src/$1',
  },
  setupFiles: ['./tests/test_helper.js'],
  setupFilesAfterEnv: ['./tests/runtime.js'],
  transform: {
    '^.+\\.js?$': 'babel-jest',
  },
  collectCoverage: true,
  collectCoverageFrom: ['src/**/*.js'],
};
