import validate from '@src/middlewares/validate.js';
import jwt from 'jsonwebtoken';

const token = jwt.sign({ id: 1 }, process.env.JWT_SECRET, {
  expiresIn: process.env.JWT_EXPIRES_IN,
});

describe('validate', () => {
  it('should validate valid token', () => {
    const { payload, expired } = validate(token);

    expect(payload).toBeDefined();
    expect(expired).toBe(false);
  });

  it('should decode correctly', () => {
    const { payload, expired } = validate(token);

    expect(payload.id).toBe(1);
    expect(expired).toBe(false);
  });

  it('should validate invalid token', () => {
    const { payload, expired } = validate('invalid-token');

    expect(payload).toBe(null);
    expect(expired).toBe(false);
  });

  it('should validate expired token', async () => {
    const expiredToken = jwt.sign({ id: 1 }, process.env.JWT_SECRET, {
      expiresIn: '1s',
    });

    await helper.pause(2);

    const { payload, expired } = validate(expiredToken);

    expect(payload).toBe(null);
    expect(expired).toBe(true);
  });
});
