import { Jwt } from './jwt.model';

describe('Jwt', () => {
  it('should create an instance', () => {
    expect(new Jwt("")).toBeTruthy();
  });
});
