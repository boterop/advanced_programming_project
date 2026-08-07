import winston from 'winston';

const { format } = winston;

const logger = winston.createLogger({
  level: 'info',
  format: format.combine(format.timestamp(), format.json()),
  transports: [
    new winston.transports.File({
      filename: 'logs/combined.log',
      format: format.printf(error => error.message),
      level: 'info',
    }),
    new winston.transports.File({
      filename: 'logs/error.log',
      format: format.printf(error => error.message),
      level: 'error',
    }),
  ],
});

if (process.env.NODE_ENV === 'dev') {
  logger.add(
    new winston.transports.Console({
      level: 'debug',
      format: format.cli(),
    }),
  );
}

export default logger;
