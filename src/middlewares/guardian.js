import validate from './validate.js';

const only_paths = ['/api'];
const ignored_paths = ['/api/docs'];

const guardian = async (req, res, next) => {
  const path = req.path;
  let useGuardian = false;

  for (const only_path of only_paths) {
    if (path.startsWith(only_path)) {
      useGuardian = true;
      break;
    }
  }

  for (const ignored_path of ignored_paths) {
    if (path.startsWith(ignored_path)) {
      useGuardian = false;
      break;
    }
  }

  if (!useGuardian) return next();

  if (!req.headers?.authorization)
    return res.status(401).send({
      error: messages.token_expired,
    });

  const { authorization } = req.headers;

  const hash = authorization.split(' ')[1];
  const token = hash || '';

  const { payload, expired } = validate(token);

  if (!payload || expired) {
    return res.status(401).send({
      error: expired ? messages.token_expired : messages.unauthorized,
    });
  }

  req.userId = payload.id;
  next();
};

export default guardian;
