import guardian from '@src/middlewares/guardian.js';
import jwt from 'jsonwebtoken';

describe('guardian', () => {
  const guardedPath = '/api/guarded';

  let testData = {};

  beforeAll(() => {
    const token = jwt.sign({ id: 1 }, process.env.JWT_SECRET, {
      expiresIn: process.env.JWT_EXPIRES_IN,
    });
    const req = { headers: { authorization: `Bearer ${token}` } };

    testData = { req };
  });

  beforeEach(() => {
    jest.resetAllMocks();

    const next = jest.fn();
    const send = jest.fn();
    const status = jest.fn().mockImplementation(_num => ({ send }));
    const res = {
      send,
      status,
    };

    testData = { ...testData, res, next };
  });

  it('should call next if path is not guarded', async () => {
    const { res, next } = testData;

    await guardian({ path: '/not-guarded' }, res, next);

    expect(next).toHaveBeenCalled();
  });

  it('should pass if path is ignored', async () => {
    const { res, next } = testData;

    await guardian({ path: '/api/docs' }, res, next);

    expect(next).toHaveBeenCalled();
  });

  it('should call next if path is guarded and token is valid', async () => {
    const { req, res, next } = testData;

    await guardian({ ...req, path: guardedPath }, res, next);

    expect(next).toHaveBeenCalled();
  });

  it('should send an error if path is guarded and token is not given', async () => {
    const { res, next } = testData;

    await guardian({ path: guardedPath }, res, next);

    expect(next).not.toHaveBeenCalled();
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.send).toHaveBeenCalledWith({
      error: messages.token_expired,
    });
  });

  it('should send an error if path is guarded and token is expired', async () => {
    const { res, next } = testData;

    const token = jwt.sign({ id: 1 }, process.env.JWT_SECRET, {
      expiresIn: '1s',
    });
    const req = {
      path: guardedPath,
      headers: { authorization: `Bearer ${token}` },
    };

    await helper.pause(2);

    await guardian(req, res, next);

    expect(next).not.toHaveBeenCalled();
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.send).toHaveBeenCalledWith({
      error: messages.token_expired,
    });
  });

  it('should send an error if path is guarded and token is invalid', async () => {
    const { res, next } = testData;

    await guardian(
      { path: guardedPath, headers: { authorization: 'Bearer invalid-token' } },
      res,
      next,
    );

    expect(next).not.toHaveBeenCalled();
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.send).toHaveBeenCalledWith({
      error: messages.unauthorized,
    });
  });

  it('should send an error with malformed token', async () => {
    const { res, next } = testData;

    await guardian(
      { path: guardedPath, headers: { authorization: 'malformed-token' } },
      res,
      next,
    );

    expect(next).not.toHaveBeenCalled();
    expect(res.status).toHaveBeenCalledWith(401);
    expect(res.send).toHaveBeenCalledWith({
      error: messages.unauthorized,
    });
  });
});
