import messages from '@src/constants/messages.js';

describe('messages', () => {
  it('should have messages', () => {
    expect(messages).toBeDefined();
    expect(Object.keys(messages).length).toBeGreaterThan(0);
  });

  it('should access to messages', () => {
    expect(messages.not_found).toBeDefined();
    expect(messages['not_found']).toBeDefined();

    expect(messages.not_found).toBe(messages['not_found']);
  });
});
