import '@src/config/globals.js';

describe('globals', () => {
  it('should have messages', () => {
    expect(messages).toBeDefined();
  });

  it('should have logger', () => {
    expect(logger).toBeDefined();
  });
});
