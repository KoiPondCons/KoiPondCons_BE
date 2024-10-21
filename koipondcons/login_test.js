Feature('Login API Test');

let headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'Authorization': ''
}
let token;


Scenario('Successful registration with valid credentials', async ({ I }) => {
    const registerPayload = {
        email: 'hoa3@example.com',
        name: 'Hòa',
        password: '123456',
        phone: '0896671154',
        role: 'CUSTOMER'
    };

    const response = await I.sendPostRequest('/register', registerPayload);

    I.seeResponseCodeIs(200);

});

Scenario("Login Test",async ({ I }) => {
    const loginPayload = {
        email: 'customer@gmail.com',
        password: '123456'
    };
    const loginResponse = await I.sendPostRequest('/login', loginPayload);
    I.seeResponseCodeIs(200);

    token = loginResponse.data.token;
    headers['Authorization'] = `Bearer ${token}`;
    console.log(token);
});


Scenario('Successful deletion of existing item', async ({ I }) => {
    const idToDelete = 15;

    const response = await I.sendDeleteRequest(`/delete/${idToDelete}`, headers);

    I.seeResponseCodeIs(200);

    I.seeResponseEquals('Xóa rùi')
});
