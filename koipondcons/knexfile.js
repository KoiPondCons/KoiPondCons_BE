// Update with your config settings.

/**
 * @type { Object.<string, import("knex").Knex.Config> }
 */
module.exports = {

  development: {
    client: 'mysql',
    connection: {
      filename: './dev.mysql'
    }
  },

  staging: {
    client: 'mysql',
    connection: {
      database: 'jdbc:mysql://root:BoVwEVYUnxWAewnRYApVmXllyarueojQ@autorack.proxy.rlwy.net:26357/railway',
      user: 'root',
      password: 'BoVwEVYUnxWAewnRYApVmXllyarueojQ'
    }
  }

};
