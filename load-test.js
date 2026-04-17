import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 50,
  iterations: 1000,
};

export default function () {
  const payload = JSON.stringify({
    firstName: `User${__VU}-${__ITER}`,
    password: `User${__VU}-${__ITER}`
  });

  const apiPathTarget = 'http://localhost:8099/users';

  const res = http.post(apiPathTarget, payload, {
    headers: { 'Content-Type': 'application/json' },
  });

  if (res.status !== 200) {
    console.log(`status=${res.status} | body=${res.body.substring(0, 100)}`);
  }

  check(res, {
    'status 200': (r) => r.status === 200,
  });

  sleep(Math.random() * 2 + 1);
}
//instalar com  sudo snap install k6
//rodar com k6 run load-test.js