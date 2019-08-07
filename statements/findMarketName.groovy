def params = [:]

params['ConfigFile'] = 'dev/us-east-1/dev01-us.tf'

if (params['ConfigFile'].split('/')[-1].contains('-')) {
  def market = params['ConfigFile'].split('/')[-1].split('-')[-1].replace('.tf', '')
  println(market)
}
