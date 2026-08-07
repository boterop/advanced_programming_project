import jwt from 'jsonwebtoken';

const validate = token => {
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    return { payload: decoded, expired: false };
  } catch (error) {
    return { payload: null, expired: error.name === 'TokenExpiredError' };
  }
};

export default validate;
