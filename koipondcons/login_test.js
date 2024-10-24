Feature('Login API Test');

let headers = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    'Authorization': ''
}
let token;

// Scenario('Successful registration with valid credentials', async ({ I }) => {
//     const registerPayload = {
//         email: 'hoa4@example.com',
//         name: 'Hòa',
//         password: '123456',
//         phone: '0896671154',
//         role: 'CUSTOMER'
//     };
//
//     const response = await I.sendPostRequest('/register', registerPayload);
//
//     I.seeResponseCodeIs(200);
//
// });

Scenario("Login Test",async ({ I }) => {
    const loginPayload = {
        email: 'manager@gmail.com',
        password: '123456'
    };
    const loginResponse = await I.sendPostRequest('/login', loginPayload);
    I.seeResponseCodeIs(200);

    token = loginResponse.data.token;
    headers['Authorization'] = `Bearer ${token}`;
    console.log(token);
});


Scenario('Successful deletion of existing item', async ({ I }) => {
    const idToDelete = 10;

    const response = await I.sendDeleteRequest(`/delete/${idToDelete}`, headers);

    I.seeResponseCodeIs(200);

    I.seeResponseEquals('Xóa rùi')
});

Feature('Fetch all accounts');

Scenario('Successfully fetch all accounts', ({I}) => {
    I.sendGetRequest('/accounts')
        .then(response => {
            I.seeResponseStatusIs(200);
            I.seeResponseContainsJson({ data: [
                    {
                        "id": 1,
                        "name": "Tran Kim Nha",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "customer@gmail.com",
                        "phone": "0933102278",
                        "address": null,
                        "role": "CUSTOMER",
                        "dateCreate": "2024-10-21",
                        "token": null
                    },
                    {
                        "id": 2,
                        "name": "Dũng",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "consultant@gmail.com",
                        "phone": "0933102278",
                        "address": null,
                        "role": "CONSULTANT",
                        "dateCreate": "2024-10-21",
                        "token": null
                    },
                    {
                        "id": 3,
                        "name": "Hoa",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "constructor@gmail.com",
                        "phone": "0896671154",
                        "address": null,
                        "role": "CONSTRUCTOR",
                        "dateCreate": "2024-10-22",
                        "token": null
                    },
                    {
                        "id": 4,
                        "name": "Mai Phạm Nồng Hậu",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "manager@gmail.com",
                        "phone": "0964254678",
                        "address": null,
                        "role": "MANAGER",
                        "dateCreate": "2024-10-22",
                        "token": null
                    },
                    {
                        "id": 5,
                        "name": "Võ Nguyễn Anh Khoa",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "designer@gmail.com",
                        "phone": "0935246215",
                        "address": null,
                        "role": "DESIGNER",
                        "dateCreate": "2024-10-22",
                        "token": null
                    },
                    {
                        "id": 6,
                        "name": "Phạm Phú Quý",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "constructor2@gmail.com",
                        "phone": "0933102278",
                        "address": null,
                        "role": "CONSTRUCTOR",
                        "dateCreate": "2024-10-23",
                        "token": null
                    },
                    {
                        "id": 7,
                        "name": "Dang Thanh An",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "constructor3@gmail.com",
                        "phone": "0933102278",
                        "address": null,
                        "role": "CONSTRUCTOR",
                        "dateCreate": "2024-10-23",
                        "token": null
                    },
                    {
                        "id": 8,
                        "name": "Hòa",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "hoa3@example.com",
                        "phone": "0896671154",
                        "address": null,
                        "role": "CUSTOMER",
                        "dateCreate": "2024-10-23",
                        "token": null
                    },
                    {
                        "id": 10,
                        "name": "Hòa",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "hoa4@example.com",
                        "phone": "0896671154",
                        "address": null,
                        "role": "CUSTOMER",
                        "dateCreate": "2024-10-23",
                        "token": null
                    }
                ] });
        });
});

Feature('Fetch an account by ID');

Scenario('Successfully fetch an account by ID', ({I}) => {
    const accountId = 1;
    I.sendGetRequest(`/accounts/${accountId}`)
        .then(response => {
            I.seeResponseStatusIs(200);
            I.seeResponseContainsJson({ data: [
                    {
                        "id": 1,
                        "name": "Tran Kim Nha",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "customer@gmail.com",
                        "phone": "0933102278",
                        "address": null,
                        "role": "CUSTOMER",
                        "dateCreate": "2024-10-21",
                        "token": null
                    }
                ]  });
        });
});

Feature('Fetch an account by name');

Scenario('Successfully fetch an account by name', ({I}) => {
    const accountName = 'Nha';
    I.sendGetRequest(`/name/${accountName}`)
        .then(response => {
            I.seeResponseStatusIs(200);
            I.seeResponseContainsJson({ data: [
                    {
                        "id": 1,
                        "name": "Tran Kim Nha",
                        "avatar": "https://t3.ftcdn.net/jpg/05/87/76/66/360_F_587766653_PkBNyGx7mQh9l1XXPtCAq1lBgOsLl6xH.jpg",
                        "email": "customer@gmail.com",
                        "phone": "0933102278",
                        "address": null,
                        "role": "CUSTOMER",
                        "dateCreate": "2024-10-21",
                        "token": null
                    }
                ]});
        });
});


